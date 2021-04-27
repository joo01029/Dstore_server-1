package gg.jominsubyungsin.admin.service.project;

import gg.jominsubyungsin.domain.entity.ProjectEntity;

import java.util.List;

public interface AdminPJService {
    List<ProjectEntity> getProjectAll();

    List<ProjectEntity> getProjectById(Long id);

    List<ProjectEntity> dropProject(Long id);
}
