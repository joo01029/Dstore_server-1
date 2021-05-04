package gg.jominsubyungsin.controller;

import gg.jominsubyungsin.domain.dto.file.FileDto;
import gg.jominsubyungsin.domain.dto.query.SelectProjectDto;
import gg.jominsubyungsin.domain.dto.user.UserDto;
import gg.jominsubyungsin.domain.dto.user.UserUpdateDto;
import gg.jominsubyungsin.domain.dto.query.SelectUserDto;
import gg.jominsubyungsin.domain.dto.user.response.UserDetailResponseDto;
import gg.jominsubyungsin.domain.entity.UserEntity;
import gg.jominsubyungsin.lib.Hash;
import gg.jominsubyungsin.domain.response.Response;
import gg.jominsubyungsin.domain.response.user.ShowUserListResponse;
import gg.jominsubyungsin.domain.response.user.ShowUserResponse;
import gg.jominsubyungsin.domain.response.user.UserDetailResponse;
import gg.jominsubyungsin.service.file.FileService;
import gg.jominsubyungsin.service.follow.FollowService;
import gg.jominsubyungsin.service.jwt.JwtService;
import gg.jominsubyungsin.service.multipart.MultipartService;
import gg.jominsubyungsin.service.project.ProjectService;
import gg.jominsubyungsin.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@ResponseBody
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserService userService;
	@Autowired
	JwtService jwtService;
	@Autowired
	ProjectService projectService;
	@Autowired
	MultipartService multipartService;
	@Autowired
	FileService fileService;
	@Autowired
	FollowService followService;
	@Autowired
	Hash hash;

	/*
	자기 소개 변경
	 */
	@PutMapping("/set/introduce")
	public Response setIntroduce(@RequestBody UserDto userDto, HttpServletRequest request) {
		Response response = new Response();


		try {
			UserEntity user = (UserEntity) request.getAttribute("user");
			userDto.setEmail(user.getEmail());

			boolean setIntruduceResult = userService.userUpdateIntroduce(userDto);

			if (!setIntruduceResult) {
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 이메일");
			}

			response.setHttpStatus(HttpStatus.OK);
			response.setMessage("자기 소개 변경 성공");

			return response;
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	비밀번호 또는 이름 변경
	 */
	@PutMapping("/update")
	public Response userUpdate(@RequestBody UserUpdateDto userUpdateDto) {
		Response response = new Response();

		try {
			String hashNowPassword = hash.hashText(userUpdateDto.getPassword());
			String hashChangePassword = userUpdateDto.getChangePassword() != null ? hash.hashText(userUpdateDto.getChangePassword()) : null;
			userUpdateDto.setPassword(hashNowPassword);
			userUpdateDto.setChangePassword(hashChangePassword);

			boolean userUpdate = userService.userUpdate(userUpdateDto);
			if (!userUpdate) {
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "이메일 또는 비밀번호가 틀림");
			}

			response.setHttpStatus(HttpStatus.OK);
			response.setMessage("유저 업데이트 성공");
			return response;
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	유저 삭제
	 */
	@DeleteMapping("/delete")
	public Response userDelete(@RequestBody UserDto userDto) {
		Response response = new Response();

		try {
			String hashPassword = hash.hashText(userDto.getPassword());
			userDto.setPassword(hashPassword);

			boolean userDeleteReuslt = userService.userDelete(userDto);

			response.setMessage("유저 삭제 성공");
			response.setHttpStatus(HttpStatus.OK);
			return response;
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	/*
	유저 보기
	 */
	@GetMapping("/show")
	public ShowUserResponse showUser(@RequestParam Long id, HttpServletRequest request) {
		ShowUserResponse showUserResponse = new ShowUserResponse();
		UserEntity user = (UserEntity) request.getAttribute("user");
		try {
			SelectUserDto selectUser = userService.findUser(id,user);

			showUserResponse.setHttpStatus(HttpStatus.OK);
			showUserResponse.setMessage("성공");
			showUserResponse.setUser(selectUser);
			return showUserResponse;
		} catch (HttpServerErrorException e) {
			throw e;
		}
	}

	/*
	유저 이름으로 찾기
	 */
	@GetMapping("/find/name")
	public ShowUserListResponse showUserList(@RequestParam String name, HttpServletRequest request) {
		ShowUserListResponse showUserListResponse = new ShowUserListResponse();
		try {
			UserEntity user = (UserEntity) request.getAttribute("user");
			List<SelectUserDto> userList = userService.findUserLikeName(name, user.getEmail(), user);

			showUserListResponse.setHttpStatus(HttpStatus.OK);
			showUserListResponse.setMessage("성공");
			showUserListResponse.setUserList(userList);
			return showUserListResponse;
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	유저 상세 페이지
	 */
	@GetMapping("/detail/{id}")
	public UserDetailResponse detailUser(@PathVariable("id") Long id, Pageable pageable, HttpServletRequest request) {
		UserDetailResponse response = new UserDetailResponse();
		UserDetailResponseDto userDetailResponseDto;
		try {
			UserEntity user = (UserEntity) request.getAttribute("user");
			//내 프로필인지 검사
			boolean myProfile = userService.checkUserSame(user.getEmail(), id);
			UserEntity profile = userService.findUserById(id);

			List<SelectProjectDto> selectProjectDetailDtos = projectService.getProjects(pageable, user, profile);
			Long follower = followService.countFollower(id);
			Long following = followService.countFollowing(id);
			userDetailResponseDto = new UserDetailResponseDto(profile, myProfile, selectProjectDetailDtos, follower,following);

			Long projectNumber = projectService.countProject(user);
			Boolean end = projectNumber < (long) pageable.getPageSize() *(pageable.getPageNumber()+1);

			response.setHttpStatus(HttpStatus.OK);
			response.setMessage("성공");
			response.setUser(userDetailResponseDto);
			response.setEnd(end);
			return response;
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	프로필 이미지 변경
	 */
	@PutMapping("/profile/image")
	public Response updateProfileImage(HttpServletRequest request, @ModelAttribute MultipartFile file) {
		Response response = new Response();

		if (file.isEmpty()) {
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "파일이 없음");
		}

		try {
			UserEntity user = (UserEntity) request.getAttribute("user");

			FileDto profileImage = multipartService.uploadSingle(file);
			userService.updateProfileImage(user.getEmail(), profileImage.getFileLocation());

			response.setHttpStatus(HttpStatus.OK);
			response.setMessage("성공");
			return response;
		} catch (Exception e) {
			throw e;
		}
	}
	/*
	팔로우
	 */
	@PutMapping("/follow/{id}")
	public Response follow(HttpServletRequest request,@PathVariable Long id){
		Response response = new Response();
		UserEntity user = (UserEntity) request.getAttribute("user");
		try{
			followService.ChangeFollowState(user, id);

			response.setHttpStatus(HttpStatus.OK);
			response.setMessage("성공");
			return response;
		}catch (Exception e) {
			throw e;
		}
	}

}

