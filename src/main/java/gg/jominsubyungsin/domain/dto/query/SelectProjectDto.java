package gg.jominsubyungsin.domain.dto.query;

import gg.jominsubyungsin.domain.entity.FileEntity;
import gg.jominsubyungsin.domain.entity.ProjectEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SelectProjectDto {
  private Long id;
  private String title;
  private String content;

  private FileEntity mainPhoto;
  private List<FileEntity> files = new ArrayList<>();

  private List<SelectUserDto> users = new ArrayList<>();

  public SelectProjectDto(ProjectEntity projectEntity, List<SelectUserDto> selectUserDto){
    this.id = projectEntity.getId();
    this.title = projectEntity.getTitle();
    this.content = projectEntity.getContent();
    this.mainPhoto = projectEntity.getFiles().get(0);
    this.files = projectEntity.getFiles();
    this.users = selectUserDto;
  }
}
