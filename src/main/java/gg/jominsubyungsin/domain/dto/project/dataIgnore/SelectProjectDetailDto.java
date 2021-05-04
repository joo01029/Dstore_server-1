package gg.jominsubyungsin.domain.dto.project.dataIgnore;

import gg.jominsubyungsin.domain.dto.user.dataIgnore.SelectUserDto;
import gg.jominsubyungsin.domain.entity.FileEntity;
import gg.jominsubyungsin.domain.entity.ProjectEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class SelectProjectDetailDto {
	private Long id;
	private String title;
	private String content;
	private Date createAt;
	private FileEntity mainPhoto;
	private List<FileEntity> files = new ArrayList<>();

	private List<SelectUserDto> users = new ArrayList<>();

	public SelectProjectDetailDto(ProjectEntity projectEntity, List<SelectUserDto> selectUserDto) {
		this.id = projectEntity.getId();
		this.title = projectEntity.getTitle();
		this.content = projectEntity.getContent();
		this.createAt = projectEntity.getCreateAt();
		this.mainPhoto = projectEntity.getFiles().get(0);
		this.files = projectEntity.getFiles();
		this.users = selectUserDto;
	}
}
