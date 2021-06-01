package gg.jominsubyungsin.controller;

import gg.jominsubyungsin.domain.dto.email.request.SendEmailDto;
import gg.jominsubyungsin.domain.dto.token.LoginJwtDto;
import gg.jominsubyungsin.domain.dto.user.request.LoginDto;
import gg.jominsubyungsin.domain.dto.user.request.UserDto;
import gg.jominsubyungsin.domain.response.Response;
import gg.jominsubyungsin.domain.dto.user.response.LoginResponse;
import gg.jominsubyungsin.lib.Log;
import gg.jominsubyungsin.service.auth.AuthService;
import gg.jominsubyungsin.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

import javax.servlet.http.HttpServletRequest;


@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
	private final JwtService jwtService;
	private final AuthService authService;
	private final Log log;
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
		} catch (Exception e) {
			log.error("error at POST /auth/create controller");
			e.printStackTrace();

			throw e;
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
		} catch (Exception e) {
			log.error("error at POST /auth/login controller");
			throw e;
		}
	}

	/*
	 *토큰 재생성
	 */
	@GetMapping("/refresh")
	public LoginResponse tokenRefresh(HttpServletRequest request) {
		LoginResponse loginResponse = new LoginResponse();
		try {
			String Authorization = request.getHeader("Authorization");
			System.out.println(Authorization);
			String subject = jwtService.refreshTokenDecoding(Authorization.trim());
			LoginJwtDto tokens = authService.MakeTokens(subject);

			loginResponse.setTokens(tokens);
			loginResponse.setMessage("성공");
			loginResponse.setHttpStatus(HttpStatus.OK);
			return loginResponse;
		} catch (Exception e) {
			log.error("error at GET /auth/refresh controller");
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
			log.error("error at POST /auth/email controller");
			throw e;
		}
	}
}
