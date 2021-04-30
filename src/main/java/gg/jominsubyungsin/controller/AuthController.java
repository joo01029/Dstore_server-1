package gg.jominsubyungsin.controller;

import gg.jominsubyungsin.domain.dto.email.SendEmailDto;
import gg.jominsubyungsin.domain.dto.user.UserDto;

import gg.jominsubyungsin.domain.entity.UserEntity;

import gg.jominsubyungsin.enums.JwtAuth;
import gg.jominsubyungsin.lib.Hash;
import gg.jominsubyungsin.domain.response.Response;
import gg.jominsubyungsin.domain.response.user.LoginResponse;
import gg.jominsubyungsin.service.auth.AuthService;
import gg.jominsubyungsin.service.jwt.JwtService;
import gg.jominsubyungsin.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@ResponseBody
@RequestMapping("/auth")
public class AuthController {
  @Autowired UserService userService;
  @Autowired JwtService jwtService;
  @Autowired AuthService authService;
  @Autowired Hash hash;
  /*
    회원가입
   */
  @PostMapping("/create")
  public Response userCreate(@RequestBody UserDto userDto){
    Response response = new Response();

    try {
      String hashPassword = hash.hashText(userDto.getPassword());
      userDto.setPassword(hashPassword);

      authService.userCreate(userDto);

      response.setMessage("유저 저장 성공");
      response.setHttpStatus(HttpStatus.OK);
      return response;
    }catch (HttpClientErrorException | HttpServerErrorException e){
      throw e;
    }catch (Exception e){
      e.printStackTrace();
      throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
    }
  }
  /*
  * 로그인
  */
  @PostMapping("/login")
  public LoginResponse login(@RequestBody UserDto userDto){
    LoginResponse loginResponse = new LoginResponse();

    UserEntity findUserResponse;
    try {
      //비밀번호 암호화
      String hashPassword = hash.hashText(userDto.getPassword());
      userDto.setPassword(hashPassword);

      findUserResponse = authService.login(userDto);
    }catch (HttpClientErrorException | HttpServerErrorException e){
      throw e;
    }catch (Exception e){
      throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
    }
    //토큰
    String subject;
    String accessToken;
    long accessExpiredTime = 40 * 60 * 1000L;
    String refreshToken;
    long refreshExpiredTime = 7 * 24 * 60 * 60 * 1000L;
    //token발행
    try {
      subject = findUserResponse.getEmail();
      accessToken = jwtService.createToken(subject, accessExpiredTime, JwtAuth.ACCESS);
      refreshToken = jwtService.createToken(subject, refreshExpiredTime, JwtAuth.REFRESH);
    }catch (Exception e){
      throw e;
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

  /*
  토큰 재생성
   */
  @GetMapping("/refresh")
  public LoginResponse tokenRefresh(@RequestHeader String Authorization){
    LoginResponse loginResponse = new LoginResponse();

    String subject;
    try {
      subject = jwtService.refreshTokenDecoding(Authorization);
    }catch (Exception e){
      throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "토큰 디코딩 에러");
    }

    long accessExpiredTime = 20 * 60 * 1000L;
    String accessToken;
    long refreshExpiredTime = 7 * 24 * 60 * 60 * 1000L;
    String refreshTokenRemake;

    try{
      accessToken = jwtService.createToken(subject, accessExpiredTime, JwtAuth.ACCESS);
      refreshTokenRemake = jwtService.createToken(subject, refreshExpiredTime, JwtAuth.REFRESH);
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

  /*
  이메일 인증 주소 보내기
   */
  @PostMapping("/send/email")
  public Response sendEmail(@RequestBody SendEmailDto sendEmailDto){
    Response response = new Response();

    try {
      authService.sendMail(sendEmailDto.getEmail());
    }catch (HttpServerErrorException e){
      throw e;
    }
    response.setMessage("이메일 보내기 성공");
    response.setHttpStatus(HttpStatus.OK);
    return response;
  }

}
