package gg.jominsubyungsin.domain.dto.project.response;

import gg.jominsubyungsin.domain.dto.project.dataIgnore.SelectProjectDto;
import gg.jominsubyungsin.domain.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetProjectResponse extends Response {
	private List<SelectProjectDto> projectList;
	private Boolean end;
}
