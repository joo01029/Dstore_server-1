package gg.jominsubyungsin.domain.dto.project;

import gg.jominsubyungsin.domain.entity.FileEntity;
import gg.jominsubyungsin.domain.entity.ProjectEntity;
import gg.jominsubyungsin.domain.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.io.File;
import java.util.List;

@Getter
@Setter
public class GetProjectDto {
	@NotBlank
	private String title;
	@NotBlank
	private String content;
	@NotBlank
	private List<Long> users;
	@NotBlank
	private List<MultipartFile> files;

	public ProjectEntity toEntity(List<UserEntity> userEntities, List<FileEntity> fileEntities) {
		return ProjectEntity.builder()
				.title(title)
				.content(content)
				.users(userEntities)
				.files(fileEntities)
				.build();
	}
}
