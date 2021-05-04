package gg.jominsubyungsin.domain.dto.project.dataIgnore;

import gg.jominsubyungsin.domain.dto.user.dataIgnore.SelectUserDto;
import gg.jominsubyungsin.domain.entity.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ProjectDto {

	private Long id;
	private String title;
	private String content;
	private Date createAt;
	private List<SelectUserDto> users;
	private List<FileEntity> files;
	private Boolean likeState;
	private Long likeNum;
	private Long commentNum;
	private Integer commentsNum = 0;

	public ProjectDto(ProjectEntity projectEntity, List<SelectUserDto> users, Long likeNum, Boolean likeState, Long commentNum, UserEntity me) {
		id = projectEntity.getId();
		title = projectEntity.getTitle();
		content = projectEntity.getContent();
		files = projectEntity.getFiles();
		this.createAt = projectEntity.getCreateAt();
		this.likeNum = likeNum;
		this.likeState = likeState;
		this.commentNum = commentNum;
		this.users = users;

	}
}
