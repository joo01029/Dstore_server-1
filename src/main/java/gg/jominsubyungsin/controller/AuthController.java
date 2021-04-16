package gg.jominsubyungsin.controller;

import gg.jominsubyungsin.domain.dto.email.EmailDto;
import gg.jominsubyungsin.domain.dto.user.UserDto;
import gg.jominsubyungsin.domain.entity.UserEntity;

import gg.jominsubyungsin.response.Response;
import gg.jominsubyungsin.response.user.LoginResponse;
import gg.jominsubyungsin.service.email.EmailService;
import gg.jominsubyungsin.service.jwt.JwtService;
import gg.jominsubyungsin.service.security.SecurityService;
import gg.jominsubyungsin.service.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
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
  EmailService emailService;
  @Autowired
  JwtService jwtService;
  //유저 생성
  @PostMapping("/create")
  public Response userCreate(@RequestBody UserDto userDto){
    Response response = new Response();

    try{
      String hashPassword = securityService.hashPassword(userDto.getPassword());
      userDto.setPassword(hashPassword);
    }catch (HttpServerErrorException e){
      throw e;
    }

    try {
      boolean userCreateResult = userService.userCreate(userDto);
      response.setResult(true);
      response.setMessage("유저 저장 성공");
      response.setHttpStatus(HttpStatus.OK);
      response.setStatus(HttpStatus.OK.value());

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

    //비밀번호 암호화러
    try {
      String hashPassword = securityService.hashPassword(userDto.getPassword());
      userDto.setPassword(hashPassword);
    }catch (HttpServerErrorException e){
      throw e;
    }

    UserEntity findUserResponse;
    try {
      findUserResponse = userService.login(userDto);
    }catch (ResponseStatusException e){
      throw e;
    }catch (Exception e){
      throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
    }

    String subject;
    String accessToken;
    long accessExpiredTime = 20 * 60 * 1000L;

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

    loginResponse.setResult(false);
    loginResponse.setMessage("로그인 성공");
    loginResponse.setHttpStatus(HttpStatus.OK);
    loginResponse.setStatus(HttpStatus.OK.value());
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

    loginResponse.setResult(false);
    loginResponse.setMessage("로그인 성공");
    loginResponse.setHttpStatus(HttpStatus.OK);
    loginResponse.setStatus(HttpStatus.OK.value());
    loginResponse.setExepiration(expiredTime);
    loginResponse.setAccessToken(accessToken);
    loginResponse.setRefreshToken(refreshTokenRemake);
    return loginResponse;
  }

  @Value("${spring.mail.username}")
  private String email;

  @GetMapping("/send/email")
  public Response sendEmail(@RequestParam String mail){
    Response response = new Response();

    System.out.println(mail);

    String authKey;
    try {
      authKey = securityService.hashPassword(mail);

    }catch (Exception e){
      throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "키 인증 에러");
    }
    EmailDto emailDto = new EmailDto();
    emailDto.setSenderMail(email);
    emailDto.setReceiveMail(mail);
    emailDto.setSubject("이메일 인증");
    emailDto.setSenderName("D-Store");
    emailDto.setMessage(new StringBuffer().append("<h1>회원가입 인증메일입니다.</h1>")
            .append("<p>밑의 링크를 클릭하면 메일이 인증 됩니다.</p>")
            .append("<a href=`http://localhost:8080/auth/email?email="+mail)
            .append("&authKey="+authKey).append(">메일 인증 링크</a>")
            .toString());

    try {
      emailService.sendMail(emailDto);
    }catch (HttpServerErrorException e){
      throw e;
    }
    response.setMessage("이메일 보내기 성공");
    response.setHttpStatus(HttpStatus.OK);
    response.setStatus(HttpStatus.OK.value());
    response.setResult(true);
    return response;
  }
  @RequestMapping("/email")
  public Response emailAccess(@RequestParam String email, @RequestParam String authKey){
    Response response = new Response();

    String makeAuthKey;
    try{
      makeAuthKey = securityService.hashPassword(email);
    }catch (Exception e){
      throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "키 인증 에러");
    }

    if(!makeAuthKey.equals(authKey)){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"다른 키");
    }
    boolean MailAccess;
    try {
      MailAccess = userService.userMailAccess(email);
    }catch (HttpServerErrorException e){
      throw e;
    }

    if(!MailAccess){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이메일을 찾을 수 없습니다");
    }
    response.setResult(true);
    response.setMessage("메일인증 성공");
    response.setHttpStatus(HttpStatus.OK);
    response.setStatus(HttpStatus.OK.value());
    return response;
  }

}
