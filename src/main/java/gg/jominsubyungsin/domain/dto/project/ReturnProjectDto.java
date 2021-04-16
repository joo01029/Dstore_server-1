package gg.jominsubyungsin.domain.dto.project;

import gg.jominsubyungsin.domain.entity.FileEntity;
import gg.jominsubyungsin.domain.entity.ProjectEntity;
import gg.jominsubyungsin.domain.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class ReturnProjectDto {
  private Long id;
  private String title;
  private String content;
  private List<Long> users;
  private List<MultipartFile> files;

  public ProjectEntity toEntity(List<UserEntity> userEntities, List<FileEntity> fileEntities){
    return ProjectEntity.builder()
            .id(id)
            .title(title)
            .content(content)
            .users(userEntities)
            .files(fileEntities)
            .build();
  }
}
