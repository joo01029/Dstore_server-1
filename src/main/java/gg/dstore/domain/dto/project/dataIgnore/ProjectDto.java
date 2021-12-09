package gg.dstore.domain.dto.project.dataIgnore;

import gg.dstore.domain.dto.user.dataIgnore.SelectUserDto;
import gg.dstore.domain.entity.*;
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
	private List<String> tags;
	private List<SelectUserDto> users;
	private List<FileEntity> files;
	private Boolean likeState;
	private Long likeNum;
	private Long commentNum;

	public ProjectDto(ProjectEntity projectEntity, List<SelectUserDto> users, Long likeNum, Boolean likeState, Long commentNum, List<String> tags) {
		id = projectEntity.getId();
		title = projectEntity.getTitle();
		content = projectEntity.getContent();
		files = projectEntity.getFiles();
		this.createAt = projectEntity.getCreateAt();
		this.likeNum = likeNum;
		this.likeState = likeState;
		this.commentNum = commentNum;
		this.users = users;
		this.tags = tags;
	}
}
