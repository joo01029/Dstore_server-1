package gg.dstore.service.file;

import gg.dstore.domain.dto.file.request.FileDto;
import gg.dstore.domain.entity.BannerEntity;
import gg.dstore.domain.entity.FileEntity;
import gg.dstore.domain.entity.ProjectEntity;
import org.springframework.core.io.UrlResource;

import java.util.List;

public interface FileService {
	List<FileEntity> createFiles(List<FileDto> files);

	UrlResource loadFile(String filename);

	UrlResource loadBannerFile(String filename);

	FileEntity getFileByProject(Long id, ProjectEntity project);

	void rmFile(Long id);

	ProjectEntity addFile(ProjectEntity project, List<FileDto> files, Boolean thumnail);

	List<BannerEntity> getBannerList();
}
