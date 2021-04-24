package gg.jominsubyungsin.controller;

import gg.jominsubyungsin.domain.dto.email.SendEmailDto;
import gg.jominsubyungsin.domain.dto.user.UserDto;

import gg.jominsubyungsin.domain.entity.UserEntity;

import gg.jominsubyungsin.lib.Hash;
import gg.jominsubyungsin.domain.response.Response;
import gg.jominsubyungsin.domain.response.user.LoginResponse;
import gg.jominsubyungsin.service.auth.AuthService;
import gg.jominsubyungsin.service.jwt.JwtService;
import gg.jominsubyungsin.service.security.SecurityService;
import gg.jominsubyungsin.service.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@ResponseBody
@RequestMapping("/auth")
public class AuthController {
  @Autowired
  SecurityService securityService;
  @Autowired
  UserService userService;
  @Autowired
  JwtService jwtService;
  @Autowired
  AuthService authService;
  @Autowired
  Hash hash;

  //유저 생성
  @PostMapping("/create")
  public Response userCreate(@RequestBody UserDto userDto){
    Response response = new Response();

    try{
      String hashPassword = hash.hashText(userDto.getPassword());
      userDto.setPassword(hashPassword);
    }catch (HttpServerErrorException e){
      throw e;
    }

    try {
      authService.userCreate(userDto);
      response.setMessage("유저 저장 성공");
      response.setHttpStatus(HttpStatus.OK);

      return response;
    }catch (ResponseStatusException e){
      throw e;
    }catch (Exception e){
      e.printStackTrace();
      throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
    }
  }

  //로그인
  @PostMapping("/login")
  public LoginResponse login(@RequestBody UserDto userDto){
    LoginResponse loginResponse = new LoginResponse();
    //비밀번호 암호화
    try {
      String hashPassword = hash.hashText(userDto.getPassword());
      userDto.setPassword(hashPassword);
    }catch (HttpServerErrorException e){
      throw e;
    }

    UserEntity findUserResponse;
    try {
      findUserResponse = authService.login(userDto);
    }catch (ResponseStatusException e){
      throw e;
    }catch (Exception e){
      throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
    }

    String subject;
    String accessToken;
    long accessExpiredTime = 40 * 60 * 1000L;

    String refreshToken;
    long refreshExpiredTime = 7 * 24 * 60 * 60 * 1000L;
    //token발행
    try {
      subject = findUserResponse.getEmail();
      accessToken = jwtService.createToken(subject, accessExpiredTime, false);
      refreshToken = jwtService.createToken(subject, refreshExpiredTime, true);
    }catch (Exception e){
      e.printStackTrace();
      throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "토큰 만들기 실패");
    }
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date time = new Date(System.currentTimeMillis()+accessExpiredTime);
    String expiredTime = format.format(time);

    loginResponse.setMessage("로그인 성공");
    loginResponse.setHttpStatus(HttpStatus.OK);
    loginResponse.setExepiration(expiredTime);
    loginResponse.setAccessToken(accessToken);
    loginResponse.setRefreshToken(refreshToken);

    return loginResponse;
  }
  @GetMapping("/refresh")
  public LoginResponse tokenRefresh(@RequestHeader String Authorization){
    LoginResponse loginResponse = new LoginResponse();

    String subject;
    try {
      subject = jwtService.getRefreshTokenSubject(Authorization);
    }catch (Exception e){
      throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "토큰 디코딩 에러");
    }
    long accessExpiredTime = 20 * 60 * 1000L;
    String accessToken;

    long refreshExpiredTime = 7 * 24 * 60 * 60 * 1000L;
    String refreshTokenRemake;
    try{
      accessToken = jwtService.createToken(subject, accessExpiredTime, false);
      refreshTokenRemake = jwtService.createToken(subject, refreshExpiredTime, true);
    }catch (Exception e){
      throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "토큰 재생성 실패");
    }
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date time = new Date(System.currentTimeMillis()+accessExpiredTime);
    String expiredTime = format.format(time);

    loginResponse.setMessage("로그인 성공");
    loginResponse.setHttpStatus(HttpStatus.OK);
    loginResponse.setExepiration(expiredTime);
    loginResponse.setAccessToken(accessToken);
    loginResponse.setRefreshToken(refreshTokenRemake);

    return loginResponse;
  }



  @PostMapping("/send/email")
  public Response sendEmail(@RequestBody SendEmailDto sendEmailDto){
    Response response = new Response();

    System.out.println(sendEmailDto.getEmail());

    try {
      authService.sendMail(sendEmailDto.getEmail());
    }catch (HttpServerErrorException e){
      throw e;
    }
    response.setMessage("이메일 보내기 성공");
    response.setHttpStatus(HttpStatus.OK);
    return response;
  }

//  @RequestMapping("/email")
//  public Response emailAccess(@RequestParam String email, @RequestParam String authKey){
//    Response response = new Response();
//
//    String makeAuthKey;
//    try{
//      makeAuthKey = securityService.hashPassword(email);
//    }catch (Exception e){
//      throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "키 인증 에러");
//    }
//
//    if(!makeAuthKey.equals(authKey)){
//      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"다른 키");
//    }
//    boolean MailAccess;
//    try {
//      MailAccess = authService.userMailAccess(email);
//    }catch (HttpServerErrorException e){
//      throw e;
//    }
//
//    if(!MailAccess){
//      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이메일을 찾을 수 없습니다");
//    }
//    response.setMessage("메일인증 성공");
//    response.setHttpStatus(HttpStatus.OK);
//    return response;
//  }

}
