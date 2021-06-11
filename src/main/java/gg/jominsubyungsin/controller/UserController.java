package gg.jominsubyungsin.controller;

import gg.jominsubyungsin.domain.dto.file.request.FileDto;
import gg.jominsubyungsin.domain.dto.user.dataIgnore.SelectUserDto;
import gg.jominsubyungsin.domain.dto.user.request.UserDto;
import gg.jominsubyungsin.domain.dto.user.request.UserUpdateDto;
import gg.jominsubyungsin.domain.dto.user.response.UserDetailResponseDto;
import gg.jominsubyungsin.domain.entity.UserEntity;
import gg.jominsubyungsin.lib.Hash;
import gg.jominsubyungsin.domain.response.Response;
import gg.jominsubyungsin.domain.dto.user.response.ShowUserListResponse;
import gg.jominsubyungsin.domain.dto.user.response.ShowUserResponse;
import gg.jominsubyungsin.domain.dto.user.response.UserDetailResponse;
import gg.jominsubyungsin.lib.Log;
import gg.jominsubyungsin.service.follow.FollowService;
import gg.jominsubyungsin.service.multipart.MultipartService;
import gg.jominsubyungsin.service.project.ProjectService;
import gg.jominsubyungsin.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
	private final UserService userService;
	private final ProjectService projectService;
	private final MultipartService multipartService;
	private final Hash hash;
	private final Log log;

	/*
	 *자기 소개 변경
	 */
	@PutMapping("/introduce")
	@Transactional
	public Response setIntroduce(@RequestBody UserDto userDto, HttpServletRequest request) {
		Response response = new Response();

		try {
			UserEntity user = (UserEntity) request.getAttribute("user");
			if (user == null) {
				throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "토큰이 필요함");
			}

			userDto.setEmail(user.getEmail());

			boolean setIntruduceResult = userService.userUpdateIntroduce(userDto);

			if (!setIntruduceResult) {
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 이메일");
			}

			response.setHttpStatus(HttpStatus.OK);
			response.setMessage("자기 소개 변경 성공");
			return response;
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	/*
	 *비밀번호 또는 이름 변경
	 */
	@PutMapping("/password-and-name")
	@Transactional
	public Response updateUser(@RequestBody UserUpdateDto userUpdateDto) {
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
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	/*
	 *유저 삭제
	 */
	@DeleteMapping
	@Transactional
	public Response deleteUser(@RequestBody UserDto userDto) {
		Response response = new Response();

		try {
			boolean userDeleteReuslt = userService.userDelete(userDto);

			response.setMessage("유저 삭제 성공");
			response.setHttpStatus(HttpStatus.OK);
			return response;
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	@GetMapping("/me")
	@Transactional(readOnly = true)
	public ShowUserResponse showUser(HttpServletRequest request) {
		ShowUserResponse showUserResponse = new ShowUserResponse();
		try {
			UserEntity user = (UserEntity) request.getAttribute("user");

			if (user == null) {
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "토큰이 필요함");
			}
			SelectUserDto userDto = new SelectUserDto(user, false);

			showUserResponse.setHttpStatus(HttpStatus.OK);
			showUserResponse.setMessage("성공");
			showUserResponse.setUser(userDto);
			return showUserResponse;
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}


	/*
	 *유저 이름으로 찾기
	 */
	@GetMapping
	@Transactional(readOnly = true)
	public ShowUserListResponse showUserList(@RequestParam("name") String name, HttpServletRequest request) {
		ShowUserListResponse showUserListResponse = new ShowUserListResponse();
		try {
			UserEntity user = (UserEntity) request.getAttribute("user");
			List<SelectUserDto> userList = userService.findUserLikeName(name, user.getEmail(), user);

			showUserListResponse.setHttpStatus(HttpStatus.OK);
			showUserListResponse.setMessage("성공");
			showUserListResponse.setUserList(userList);
			return showUserListResponse;
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	/*
	 *유저 상세 페이지
	 */
	@GetMapping("/{userId}")
	@Transactional(readOnly = true)
	public UserDetailResponse detailUser(@PathVariable("userId") Long id, Pageable pageable, HttpServletRequest request) {
		UserDetailResponse response = new UserDetailResponse();
		try {
			UserEntity user = (UserEntity) request.getAttribute("user");
			UserDetailResponseDto userDetailResponseDto = userService.getUserDetail(id, user, pageable);

			Long projectNumber = projectService.countProject(user);
			Boolean end = projectNumber < (long) pageable.getPageSize() * (pageable.getPageNumber() + 1);

			response.setHttpStatus(HttpStatus.OK);
			response.setMessage("성공");
			response.setUser(userDetailResponseDto);
			response.setEnd(end);
			return response;
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	/*
	 *프로필 이미지 변경
	 */
	@PutMapping("/image")
	public Response updateProfileImage(HttpServletRequest request, @ModelAttribute MultipartFile file) {
		Response response = new Response();

		try {
			UserEntity user = (UserEntity) request.getAttribute("user");
			if (user == null) {
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "토큰이 필요함");
			}

			if (file.isEmpty()) {
				userService.updateProfileImage(user.getEmail(), null);
			} else {
				FileDto profileImage = multipartService.uploadSingle(file);
				userService.updateProfileImage(user.getEmail(), profileImage.getFileLocation());
			}
			response.setHttpStatus(HttpStatus.OK);
			response.setMessage("성공");
			return response;
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}
}

