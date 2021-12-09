package gg.jominsubyungsin.service.project;

import gg.jominsubyungsin.domain.dto.project.dataIgnore.ProjectDto;
import gg.jominsubyungsin.domain.dto.project.dataIgnore.SelectProjectDto;
import gg.jominsubyungsin.domain.dto.project.request.GetProjectDto;
import gg.jominsubyungsin.domain.dto.project.request.PutProjectDto;

import gg.jominsubyungsin.domain.entity.UserEntity;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface ProjectService {
	void saveProject(GetProjectDto projectDto, UserEntity users) throws IOException;

	List<SelectProjectDto> getProjects(Pageable pageable, UserEntity me);
	List<SelectProjectDto> getProjects(Pageable pageable, UserEntity me, String value);

	List<SelectProjectDto> getProjects(Pageable pageable, UserEntity me, UserEntity user);

	Long countProject();

	Long countProject(UserEntity user);

	Long countProject(String title);

	ProjectDto projectDetail(Long id, UserEntity user);

	void projectUpdate(Long id, UserEntity user, PutProjectDto putProjectDto) throws IOException;

	void deleteProject(Long id, UserEntity user);
}
