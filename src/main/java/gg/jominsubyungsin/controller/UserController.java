package gg.jominsubyungsin.controller;

import gg.jominsubyungsin.domain.dto.query.SelectProjectDto;
import gg.jominsubyungsin.domain.dto.user.UserDto;
import gg.jominsubyungsin.domain.dto.user.UserUpdateDto;
import gg.jominsubyungsin.domain.dto.query.SelectUserDto;
import gg.jominsubyungsin.domain.dto.user.response.UserDetailResponseDto;
import gg.jominsubyungsin.domain.entity.UserEntity;
import gg.jominsubyungsin.response.Response;
import gg.jominsubyungsin.response.user.ShowUserListResponse;
import gg.jominsubyungsin.response.user.ShowUserResponse;
import gg.jominsubyungsin.response.user.UserDetailResponse;
import gg.jominsubyungsin.service.jwt.JwtService;
import gg.jominsubyungsin.service.project.ProjectService;
import gg.jominsubyungsin.service.security.SecurityService;
import gg.jominsubyungsin.service.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Calendar;
import java.util.List;

@Controller
@ResponseBody
@RequestMapping("/user")
public class UserController {
  @Autowired
  UserService userService;
  @Autowired
  SecurityService securityService;
  @Autowired
  JwtService jwtService;
  @Autowired
  ProjectService projectService;

  @PostMapping("/set/introduce")
  public Response setIntroduce(@RequestBody UserDto userDto, @RequestHeader String Authorization){
    Response response = new Response();

    String subject = jwtService.getRefreshTokenSubject(Authorization);

    userDto.setEmail(subject);

    boolean setIntruduceResult;
    try {
      setIntruduceResult = userService.userUpdateIntroduce(userDto);
    }catch (HttpServerErrorException e){
      throw e;
    }
    if(!setIntruduceResult){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 이메일");
    }

    response.setHttpStatus(HttpStatus.OK);
    response.setMessage("자기 소개 변경 성공");

    return response;
  }
  @PutMapping("/update")
  public Response userUpdate (@RequestBody UserUpdateDto userUpdateDto){
    Response response = new Response();

    String hashNowPassword;
    String hashChangePassword;

    try {
      hashNowPassword = securityService.hashPassword(userUpdateDto.getPassword());
      hashChangePassword = userUpdateDto.getChangePassword() != null ? securityService.hashPassword(userUpdateDto.getChangePassword()) : null;
    }catch (HttpServerErrorException e){
      throw e;
    }

    userUpdateDto.setPassword(hashNowPassword);
    userUpdateDto.setChangePassword(hashChangePassword);

    boolean userUpdateResult;
    try {
      userUpdateResult = userService.userUpdate(userUpdateDto);
    }catch (HttpServerErrorException e){
      throw e;
    }
    if(!userUpdateResult){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이메일 또는 비밀번호가 틀림");
    }

    response.setHttpStatus(HttpStatus.OK);
    response.setMessage("유저 업데이트 성공");

    return response;
  }

  //유저 삭제
  @DeleteMapping("/delete")
  public Response userDelete(@RequestBody UserDto userDto){
    Response response = new Response();

    try {
      String hashPassword = securityService.hashPassword(userDto.getPassword());
      userDto.setPassword(hashPassword);
    }catch (HttpServerErrorException e){
      throw e;
    }

    try {
      boolean userDeleteReuslt = userService.userDelete(userDto);
    }catch (HttpClientErrorException e){
      throw e;
    }catch (Exception e){
      e.printStackTrace();
      throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
    }

    response.setMessage("유저 삭제 성공");
    response.setHttpStatus(HttpStatus.OK);

    return response;
  }
  @GetMapping("/show")
  public ShowUserResponse showUser(@RequestParam Long id) {
    ShowUserResponse showUserResponse = new ShowUserResponse();
    SelectUserDto selectUser;
    try {
      selectUser = userService.finduser(id);
    }catch (HttpServerErrorException e){
      throw e;
    }

    showUserResponse.setHttpStatus(HttpStatus.OK);
    showUserResponse.setMessage("성공");
    showUserResponse.setSelectUserNoPrivacy(selectUser);

    return showUserResponse;
  }
  @GetMapping("/find/name")
  public ShowUserListResponse showUserList(@RequestParam String name,@RequestHeader String Authorization){
    ShowUserListResponse showUserListResponse = new ShowUserListResponse();

    String email = jwtService.getAccessTokenSubject(Authorization);

    List<SelectUserDto> userList;
    try {
      userList = userService.findUserLikeName(name, email);
    }catch (Exception e){
      throw e;
    }

    showUserListResponse.setHttpStatus(HttpStatus.OK);
    showUserListResponse.setMessage("성공");
    showUserListResponse.setUserList(userList);

    return showUserListResponse;
  }

  @GetMapping("/detail/{id}")
  public UserDetailResponse detailUser(@PathVariable("id")Long id,Pageable pageable, @RequestHeader String Authorization){
    UserDetailResponse response = new UserDetailResponse();
    UserDetailResponseDto userDetailResponseDto;
    String subject;
    try {
      subject = jwtService.getAccessTokenSubject(Authorization);
    }catch (Exception e){
      e.printStackTrace();
      throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "토큰 디코딩 에러");
    }
    boolean myProfile;
    UserEntity profile;
    List<SelectProjectDto> selectProjectDtos;
    try {
      myProfile = userService.checkUserSame(subject, id);
      profile = userService.findUserId(id);
      selectProjectDtos = projectService.getProjects(pageable, profile);
    } catch (Exception e){
      throw e;
    }
    userDetailResponseDto = new UserDetailResponseDto(profile,myProfile,selectProjectDtos);

    response.setHttpStatus(HttpStatus.OK);
    response.setMessage("성공");
    response.setUser(userDetailResponseDto);

    return response;
  }
}

