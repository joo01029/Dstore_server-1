package gg.dstore.domain.dto.project.response;

import gg.dstore.domain.dto.project.dataIgnore.ProjectDto;
import gg.dstore.domain.response.Response;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetProjectDetailResponse extends Response {
	private ProjectDto project;
}
