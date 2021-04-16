package gg.jominsubyungsin.domain.dto.file;

import gg.jominsubyungsin.domain.entity.FileEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileDto {
  private String fileLocation;
  private String type;
  private Integer thumnail;
  private Long id;

  public FileEntity toEntity(){
    return FileEntity.builder()
            .id(id)
            .thumnail(thumnail)
            .fileLocation(fileLocation)
            .type(type)
            .build();
  }
}
