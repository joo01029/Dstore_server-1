package gg.dstore.service.multipart;

import gg.dstore.domain.dto.file.request.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MultipartService {
	FileDto uploadSingle(MultipartFile file) throws IOException;

	List<FileDto> uploadMulti(List<MultipartFile> files) throws IOException;

	String uploadBanner(MultipartFile file) throws IOException;
}
