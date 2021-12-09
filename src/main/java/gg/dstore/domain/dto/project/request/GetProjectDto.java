package gg.dstore.domain.dto.project.request;

import gg.dstore.domain.entity.FileEntity;
import gg.dstore.domain.entity.ProjectEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
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

	private List<String> tags;

	public ProjectEntity toEntity(List<FileEntity> fileEntities) {
		return ProjectEntity.builder()
				.title(title)
				.content(content)
				.files(fileEntities)
				.build();
	}
}
