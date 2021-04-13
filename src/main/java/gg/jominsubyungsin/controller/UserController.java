package gg.jominsubyungsin.controller;

import gg.jominsubyungsin.domain.dto.user.UserDto;
import gg.jominsubyungsin.domain.dto.user.UserUpdateDto;
import gg.jominsubyungsin.domain.entitiy.UserEntitiy;
import gg.jominsubyungsin.response.Response;
import gg.jominsubyungsin.response.user.LoginResponse;
import gg.jominsubyungsin.service.SecurityService;
import gg.jominsubyungsin.service.UserService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Controller
@ResponseBody
@RequestMapping("/user")
public class UserController {
  @Autowired
  UserService userService;
  @Autowired
  SecurityService securityService;

  @PostMapping("/set/introduce")
  public Response setIntroduce(@RequestBody UserDto userDto, @RequestHeader String Authorization){
    Response response = new Response();

    String subject = securityService.getRefreshTokenSubject(Authorization);

    userDto.setEmail(subject);

    boolean setIntruduceResult;
    try {
      setIntruduceResult = userService.userUpdateIntroduce(userDto);
    }catch (HttpServerErrorException e){
      throw e;
    }
    if(!setIntruduceResult){
      throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 이메일");
    }

    response.setHttpStatus(HttpStatus.OK);
    response.setStatus(HttpStatus.OK.value());
    response.setMessage("자기 소개 변경 성공");
    response.setResult(true);

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
      throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "이메일 또는 비밀번호가 틀림");
    }

    response.setResult(true);
    response.setHttpStatus(HttpStatus.OK);
    response.setStatus(HttpStatus.OK.value());
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
    response.setResult(true);
    response.setHttpStatus(HttpStatus.OK);
    response.setStatus(HttpStatus.OK.value());

    return response;
  }
}

