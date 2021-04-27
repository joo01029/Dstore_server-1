package gg.jominsubyungsin.admin.response;

import gg.jominsubyungsin.domain.entity.ProjectEntity;
import gg.jominsubyungsin.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProjectListResponse extends Response {
    private List<ProjectEntity> projectList;
}
