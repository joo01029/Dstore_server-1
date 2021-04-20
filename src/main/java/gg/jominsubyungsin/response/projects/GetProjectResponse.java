package gg.jominsubyungsin.response.projects;

import gg.jominsubyungsin.domain.dto.query.SelectProjectDto;
import gg.jominsubyungsin.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetProjectResponse extends Response {
  private List<SelectProjectDto> projectList;
}
