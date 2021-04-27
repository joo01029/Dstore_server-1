package gg.jominsubyungsin.domain.response.projects;

import gg.jominsubyungsin.domain.dto.project.GetProjectDto;
import gg.jominsubyungsin.domain.dto.project.ProjectDto;
import gg.jominsubyungsin.domain.response.Response;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GetProjectDetailResponse extends Response {
  private ProjectDto project;
}
