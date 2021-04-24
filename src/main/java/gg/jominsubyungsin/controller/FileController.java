package gg.jominsubyungsin.controller;

import gg.jominsubyungsin.service.file.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpServerErrorException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
@RequestMapping("/file")
public class FileController {
  @Autowired
  FileService fileService;

  @GetMapping("/get/{filename}")
  public ResponseEntity<UrlResource> getImage(@PathVariable String filename, HttpServletRequest request){
    UrlResource resource = fileService.loadFile(filename);
    String contentType = null;

    try{
      contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
    }catch (IOException e){
      e.printStackTrace();
      throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류");
    }
    return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .body(resource);
  }
}
