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
import gg.jominsubyungsin.service.jwt.JwtService;
import gg.jominsubyungsin.service.multipart.MultipartService;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
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
  @Autowired
  MultipartService multipartService;
  @Autowired
  FileService fileService;
  Hash hash;

  @PutMapping("/set/introduce")
  public Response setIntroduce(@RequestBody UserDto userDto, HttpServletRequest request){
    Response response = new Response();

    UserEntity user;
    try {
      user = (UserEntity) request.getAttribute("user");
    }catch (Exception e) {
      throw e;
    }
    userDto.setEmail(user.getEmail());

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
      hashNowPassword = hash.hashText(userUpdateDto.getPassword());
      hashChangePassword = userUpdateDto.getChangePassword() != null ? hash.hashText(userUpdateDto.getChangePassword()) : null;
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
      String hashPassword = hash.hashText(userDto.getPassword());
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
    showUserResponse.setUser(selectUser);

    return showUserResponse;
  }
  @GetMapping("/find/name")
  public ShowUserListResponse showUserList(@RequestParam String name,HttpServletRequest request){
    ShowUserListResponse showUserListResponse = new ShowUserListResponse();
    UserEntity user;
    try {
      user = (UserEntity) request.getAttribute("user");
    }catch (Exception e) {
      throw e;
    }
    List<SelectUserDto> userList;
    try {
      userList = userService.findUserLikeName(name, user.getEmail());
    }catch (Exception e){
      throw e;
    }

    showUserListResponse.setHttpStatus(HttpStatus.OK);
    showUserListResponse.setMessage("성공");
    showUserListResponse.setUserList(userList);

    return showUserListResponse;
  }

  @GetMapping("/detail/{id}")
  public UserDetailResponse detailUser(@PathVariable("id")Long id, Pageable pageable, HttpServletRequest request){
    UserDetailResponse response = new UserDetailResponse();
    UserDetailResponseDto userDetailResponseDto;
    UserEntity user;
    try {
      user = (UserEntity) request.getAttribute("user");
    }catch (Exception e){
      throw e;
    }
    boolean myProfile;
    UserEntity profile;
    List<SelectProjectDto> selectProjectDetailDtos;
    try {
      myProfile = userService.checkUserSame(user.getEmail(), id);
      profile = userService.findUserId(id);
      selectProjectDetailDtos = projectService.getProjects(pageable, profile);
    } catch (Exception e){
      throw e;
    }
    userDetailResponseDto = new UserDetailResponseDto(profile,myProfile, selectProjectDetailDtos);

    Boolean end = selectProjectDetailDtos.size() < pageable.getPageSize();

    response.setHttpStatus(HttpStatus.OK);
    response.setMessage("성공");
    response.setUser(userDetailResponseDto);
    response.setEnd(end);
    return response;
  }

  @PutMapping("/profile/image")
  public Response updateProfileImage(HttpServletRequest request, @ModelAttribute MultipartFile file){
    Response response = new Response();

    if(file.isEmpty()){
      throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "파일이 없음");
    }

    UserEntity user;
    try {
      user = (UserEntity) request.getAttribute("user");
    }catch (Exception e) {
      throw e;
    }
    try {
      FileDto profileImage = multipartService.uploadSingle(file);
      userService.updateProfileImage(user.getEmail(), profileImage.getFileLocation());

      response.setHttpStatus(HttpStatus.OK);
      response.setMessage("성공");
      return response;
    }catch (Exception e){
      throw e;
    }

  }
}

