package gg.jominsubyungsin.domain.dto.project;

import gg.jominsubyungsin.domain.entity.FileEntity;
import gg.jominsubyungsin.domain.entity.ProjectEntity;
import gg.jominsubyungsin.domain.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProjectDto {
	private Long id;
	private String title;
	private String content;
	private List<UserEntity> users = new ArrayList<>();
	private List<FileEntity> files = new ArrayList<>();
	private Boolean like = false;
	private Integer likeNum = 0;
	private Integer commentsNum = 0;

	public ProjectDto(ProjectEntity projectEntity) {
		id = projectEntity.getId();
		title = projectEntity.getTitle();
		content = projectEntity.getContent();
		users = projectEntity.getUsers();
		files = projectEntity.getFiles();
	}
}
