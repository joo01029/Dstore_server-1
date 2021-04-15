package gg.jominsubyungsin.controller;

import gg.jominsubyungsin.service.multipart.MultipartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;

@Controller
@ResponseBody
public class FileTestController {
  @Autowired
  MultipartService multipartService;

  @PostMapping("/file")
  public String file(@RequestBody MultipartFile file){
    try {
      return multipartService.uploadSingle(file);
    }catch (HttpClientErrorException | HttpServerErrorException e){
      throw e;
    }
  }
}
