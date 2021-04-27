package gg.jominsubyungsin.service.file;

import gg.jominsubyungsin.domain.dto.file.FileDto;
import gg.jominsubyungsin.domain.entity.FileEntity;
import org.springframework.core.io.UrlResource;

import java.util.List;

public interface FileService {
  void setProfileImage(FileDto file, String email);
  List<FileEntity> createFiles(List<FileDto> files);
  UrlResource loadFile(String filename);
}
