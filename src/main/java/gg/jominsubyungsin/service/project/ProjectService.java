package gg.jominsubyungsin.service.project;

import gg.jominsubyungsin.domain.dto.project.dataIgnore.ProjectDto;
import gg.jominsubyungsin.domain.dto.project.dataIgnore.SelectProjectDto;
import gg.jominsubyungsin.domain.entity.ProjectEntity;

import gg.jominsubyungsin.domain.entity.UserEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectService {
	void saveProject(ProjectEntity projectEntity);

	List<SelectProjectDto> getProjects(Pageable pageable,UserEntity me);

	List<SelectProjectDto> getProjects(Pageable pageable,UserEntity me, UserEntity user);

	Long countProject();
	Long countProject(UserEntity user);

	ProjectDto projectDetail(Long id, UserEntity user);
}
