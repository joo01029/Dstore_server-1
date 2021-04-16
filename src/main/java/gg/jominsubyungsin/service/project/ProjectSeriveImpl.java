package gg.jominsubyungsin.service.project;

import gg.jominsubyungsin.domain.entity.ProjectEntity;
import gg.jominsubyungsin.domain.entity.UserEntity;
import gg.jominsubyungsin.domain.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

@Service
public class ProjectSeriveImpl implements ProjectService{
  @Autowired
  ProjectRepository projectRepository;

  @Override
  public void saveProject(ProjectEntity projectEntity) {
    try {
      projectRepository.save(projectEntity);
      return;
    }catch (Exception e){
      e.printStackTrace();
      throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
    }
  }

}
