package gg.jominsubyungsin.controller;

import gg.jominsubyungsin.domain.dto.file.FileDto;
import gg.jominsubyungsin.service.multipart.MultipartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Controller
@ResponseBody
@RequestMapping
public class FileTestController {
  @Autowired
  MultipartService multipartService;

  @PostMapping("/file")
  public FileDto file(@RequestBody MultipartFile file){
    try {
      return multipartService.uploadSingle(file);
    }catch (ResponseStatusException | HttpServerErrorException e){
      throw e;
    }
  }
}
