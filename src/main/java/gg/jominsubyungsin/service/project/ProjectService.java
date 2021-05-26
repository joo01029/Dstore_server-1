package gg.jominsubyungsin.service.project;

import gg.jominsubyungsin.domain.dto.project.dataIgnore.ProjectDto;
import gg.jominsubyungsin.domain.dto.project.dataIgnore.SelectProjectDto;
import gg.jominsubyungsin.domain.dto.project.request.GetProjectDto;
import gg.jominsubyungsin.domain.dto.project.request.PutProjectDto;

import gg.jominsubyungsin.domain.entity.UserEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectService {
	void saveProject(GetProjectDto projectDto, UserEntity users);

	List<SelectProjectDto> getProjects(Pageable pageable,UserEntity me);

	List<SelectProjectDto> getProjects(Pageable pageable,UserEntity me, UserEntity user);

	Long countProject();
	Long countProject(UserEntity user);

	ProjectDto projectDetail(Long id, UserEntity user);

	void projectUpdate(Long id, UserEntity user, PutProjectDto putProjectDto);
	void deleteProject(Long id, UserEntity user);
}
