package gg.dstore.domain.dto.project.response;

import gg.dstore.domain.dto.project.dataIgnore.SelectProjectDto;
import gg.dstore.domain.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetProjectResponse extends Response {
	private List<SelectProjectDto> projectList;
	private Boolean end;
}
