package gg.jominsubyungsin.controller;

import gg.jominsubyungsin.domain.dto.email.request.SendEmailDto;
import gg.jominsubyungsin.domain.dto.jwt.request.RefreshDto;
import gg.jominsubyungsin.domain.dto.token.LoginJwtDto;
import gg.jominsubyungsin.domain.dto.user.request.LoginDto;
import gg.jominsubyungsin.domain.dto.user.request.UserDto;
import gg.jominsubyungsin.domain.response.Response;
import gg.jominsubyungsin.domain.dto.user.response.LoginResponse;
import gg.jominsubyungsin.lib.ConfirmToken;
import gg.jominsubyungsin.lib.Log;
import gg.jominsubyungsin.service.auth.AuthService;
import gg.jominsubyungsin.service.jwt.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

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
		LoginResponse response = new LoginResponse();
		try {
			LoginJwtDto tokens = authService.login(loginDto);

			response.setMessage("로그인 성공");
			response.setHttpStatus(HttpStatus.OK);
			response.setTokens(tokens);
			return response;
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	/*
	 *토큰 재생성
	 */
	@PostMapping("/refresh")
	public LoginResponse tokenRefresh(@RequestBody RefreshDto refresh) {
		LoginResponse loginResponse = new LoginResponse();
		try {
			log.info(refresh.getRefresh());
			String subject = jwtService.refreshTokenDecoding(refresh.getRefresh());
			LoginJwtDto tokens = authService.makeTokens(subject);

			loginResponse.setTokens(tokens);
			loginResponse.setMessage("성공");
			loginResponse.setHttpStatus(HttpStatus.OK);
			return loginResponse;
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
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
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}
}
