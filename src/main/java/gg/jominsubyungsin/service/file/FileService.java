package gg.jominsubyungsin.service.file;

import gg.jominsubyungsin.domain.dto.file.FileDto;
import gg.jominsubyungsin.domain.entity.FileEntity;

import java.util.List;

public interface FileService {
  List<FileEntity> createFile(List<FileDto> files);
}
