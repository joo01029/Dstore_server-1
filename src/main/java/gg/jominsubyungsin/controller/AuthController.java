package gg.jominsubyungsin.controller;

import gg.jominsubyungsin.domain.dto.email.request.SendEmailDto;
import gg.jominsubyungsin.domain.dto.token.LoginJwtDto;
import gg.jominsubyungsin.domain.dto.user.request.LoginDto;
import gg.jominsubyungsin.domain.dto.user.request.UserDto;
import gg.jominsubyungsin.domain.entity.UserEntity;
import gg.jominsubyungsin.enums.JwtAuth;
import gg.jominsubyungsin.lib.Hash;
import gg.jominsubyungsin.domain.response.Response;
import gg.jominsubyungsin.domain.dto.user.response.LoginResponse;
import gg.jominsubyungsin.service.auth.AuthService;
import gg.jominsubyungsin.service.jwt.JwtService;
import gg.jominsubyungsin.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

@RequiredArgsConstructor
@Controller
@ResponseBody
@RequestMapping("/auth")
public class AuthController {
	private final JwtService jwtService;
	private final AuthService authService;

	/*
	 *회원가입
	 */
	@PostMapping("/create")
	public Response userCreate(@RequestBody UserDto userDto) {
		Response response = new Response();

		try {
			authService.userCreate(userDto);

			response.setMessage("유저 저장 성공");
			response.setHttpStatus(HttpStatus.OK);
			return response;
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	/*
	 * 로그인
	 */
	@PostMapping("/login")
	public LoginResponse login(@RequestBody LoginDto loginDto) {
		LoginResponse loginResponse = new LoginResponse();
		try {
			LoginJwtDto tokens = authService.login(loginDto);
			loginResponse.setMessage("로그인 성공");
			loginResponse.setHttpStatus(HttpStatus.OK);
			loginResponse.setTokens(tokens);
			return loginResponse;
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			throw e;
		} catch (Exception e) {
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
		//토큰

	}

	/*
	 *토큰 재생성
	 */
	@GetMapping("/refresh")
	public LoginResponse tokenRefresh(HttpServletRequest request) {
		LoginResponse loginResponse = new LoginResponse();
		try {
			String Authorization = request.getHeader("Authorization");
			String subject = jwtService.refreshTokenDecoding(Authorization);
			LoginJwtDto tokens = authService.MakeTokens(subject);

			loginResponse.setTokens(tokens);
			loginResponse.setMessage("성공");
			loginResponse.setHttpStatus(HttpStatus.OK);
			return loginResponse;
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 *이메일 인증 주소 보내기
	 */
	@PostMapping("/email")
	public Response sendEmail(@RequestBody SendEmailDto sendEmailDto) {
		Response response = new Response();
		try {
			authService.sendMail(sendEmailDto.getEmail());

			response.setMessage("이메일 보내기 성공");
			response.setHttpStatus(HttpStatus.OK);
			return response;
		} catch (HttpServerErrorException e) {
			throw e;
		}

	}

}
