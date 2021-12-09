package gg.dstore.controller;

import gg.dstore.lib.Log;
import gg.dstore.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@RequiredArgsConstructor
@Controller
public class TemplateController {
	private final AuthService authService;
	private final Log log;

	/*
	 *이메일 인증 페이지
	 */
	@RequestMapping("/email-auth")
	public String emailAuth(@RequestParam String code, Model model) {
		try {
			Boolean isExist = authService.authEmail(code);
			model.addAttribute("isConfirm", isExist);
			return "email-auth";
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}
}
