package gg.jominsubyungsin.domain.dto.project.response;

import gg.jominsubyungsin.domain.dto.project.dataIgnore.ProjectDto;
import gg.jominsubyungsin.domain.response.Response;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetProjectDetailResponse extends Response {
	private ProjectDto project;
}
