package gg.jominsubyungsin.domain.dto.query;

import gg.jominsubyungsin.domain.entity.FileEntity;
import gg.jominsubyungsin.domain.entity.ProjectEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class SelectProjectDto {
	private Long id;
	private String title;
	private FileEntity mainPhoto;
	private Date createAt;
	private List<SelectUserDto> users;

	public SelectProjectDto(ProjectEntity projectEntity, List<SelectUserDto> selectUserDto) {
		this.id = projectEntity.getId();
		this.title = projectEntity.getTitle();
		this.createAt = projectEntity.getCreateAt();
		this.mainPhoto = projectEntity.getFiles().get(0);
		this.users = selectUserDto;
	}
}
