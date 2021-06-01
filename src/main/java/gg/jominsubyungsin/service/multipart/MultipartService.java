package gg.jominsubyungsin.service.multipart;

import gg.jominsubyungsin.domain.dto.file.request.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MultipartService {
	FileDto uploadSingle(MultipartFile file);

	List<FileDto> uploadMulti(List<MultipartFile> files);

	String uploadBanner(MultipartFile file);
}
