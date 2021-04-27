package gg.jominsubyungsin.controller;

import gg.jominsubyungsin.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpServerErrorException;

@Controller
public class TemplateController {
  @Autowired
  private AuthService authService;

  @RequestMapping("/email-auth")
  public String emailAuth(@RequestParam String code, Model model){
    try{
      Boolean isExist = authService.authEmail(code);
      model.addAttribute("isConfirm", isExist);
      return "email-auth";
    }catch (Exception e){
      e.printStackTrace();
      throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "서버 오류");
    }
  }
}
