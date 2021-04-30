package gg.jominsubyungsin.service.multipart;

import gg.jominsubyungsin.domain.dto.file.FileDto;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MultipartService {
	FileDto uploadSingle(MultipartFile file);

	List<FileDto> uploadMulti(List<MultipartFile> files);
}
