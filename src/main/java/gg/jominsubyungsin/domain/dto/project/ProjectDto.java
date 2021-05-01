package gg.jominsubyungsin.domain.dto.project;

import gg.jominsubyungsin.domain.dto.query.SelectUserDto;
import gg.jominsubyungsin.domain.entity.FileEntity;
import gg.jominsubyungsin.domain.entity.ProjectEntity;
import gg.jominsubyungsin.domain.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ProjectDto {
	private Long id;
	private String title;
	private String content;
	private Date createAt;
	private List<SelectUserDto> users = new ArrayList<>();
	private List<FileEntity> files = new ArrayList<>();
	private Boolean likeState = false;
	private Long likeNum = 0L;
	private Integer commentsNum = 0;

	public ProjectDto(ProjectEntity projectEntity, Long likeNum,Boolean likeState) {
		id = projectEntity.getId();
		title = projectEntity.getTitle();
		content = projectEntity.getContent();
		files = projectEntity.getFiles();
		this.createAt = projectEntity.getCreateAt();
		this.likeNum = likeNum;
		this.likeState = likeState;

		for(UserEntity user: projectEntity.getUsers()){
			users.add(new SelectUserDto( user));
		}
	}
}
