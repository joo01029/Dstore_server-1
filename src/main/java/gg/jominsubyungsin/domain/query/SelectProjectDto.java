package gg.jominsubyungsin.domain.query;

import gg.jominsubyungsin.domain.entity.FileEntity;
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
}
