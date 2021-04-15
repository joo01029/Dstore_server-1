package gg.jominsubyungsin.service.multipart;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MultipartService {
  String uploadSingle(MultipartFile file);
  List<String> uploadMulti(List<MultipartFile> files);
}
