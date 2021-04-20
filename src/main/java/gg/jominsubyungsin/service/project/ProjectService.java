package gg.jominsubyungsin.service.project;

import gg.jominsubyungsin.domain.entity.ProjectEntity;
import gg.jominsubyungsin.domain.dto.query.SelectProjectDto;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ProjectService {
  void saveProject(ProjectEntity projectEntity);
  List<SelectProjectDto> getProjects(Pageable pageable);
}
