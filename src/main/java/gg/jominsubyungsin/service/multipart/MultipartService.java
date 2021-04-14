package gg.jominsubyungsin.service.multipart;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public interface MultipartService {
  public String uploadSingle(@RequestParam("files") MultipartFile file) throws Exception;
}
