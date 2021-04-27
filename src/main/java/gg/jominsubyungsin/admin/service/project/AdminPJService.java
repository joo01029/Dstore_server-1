package gg.jominsubyungsin.admin.service.project;

import gg.jominsubyungsin.domain.entity.ProjectEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminPJService {
    List<ProjectEntity> getProjectAll();

//    List<ProjectEntity> getProjectById(Long id);

    List<ProjectEntity> dropProject(Long id);
}
