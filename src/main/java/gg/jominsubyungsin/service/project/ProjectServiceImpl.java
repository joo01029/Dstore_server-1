package gg.jominsubyungsin.service.project;

import gg.jominsubyungsin.domain.dto.query.SelectProjectDto;
import gg.jominsubyungsin.domain.entity.ProjectEntity;
import gg.jominsubyungsin.domain.entity.UserEntity;
import gg.jominsubyungsin.domain.dto.query.SelectProjectDetailDto;
import gg.jominsubyungsin.domain.dto.query.SelectUserDto;
import gg.jominsubyungsin.domain.repository.ProjectListRepository;
import gg.jominsubyungsin.domain.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService{
  @Autowired
  ProjectRepository projectRepository;
  @Autowired
  ProjectListRepository projectListRepository;

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

  @Override
  public List<SelectProjectDto> getProjects(Pageable pageable) {
    List<ProjectEntity> projectEntities;
    List<SelectProjectDto> projectDtos = new ArrayList<>();

    Page<ProjectEntity> projectEntityPage;

    try{
      projectEntityPage = projectListRepository.findAll(pageable);
      projectEntities = projectEntityPage.getContent();
      for(ProjectEntity projectEntity: projectEntities){
        List<SelectUserDto> userDtos = new ArrayList<>();
        for(UserEntity userEntity: projectEntity.getUsers()){
          SelectUserDto userDto = new SelectUserDto(userEntity);
          userDtos.add(userDto);
        }
        SelectProjectDto selectProjectDto = new SelectProjectDto(projectEntity, userDtos);
        projectDtos.add(selectProjectDto);
      }

      return projectDtos;
    }catch (Exception e){
      e.printStackTrace();
      throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
    }
  }

  @Override
  public List<SelectProjectDto> getProjects(Pageable pageable, UserEntity user) {
    List<ProjectEntity> projectEntities;
    List<SelectProjectDto> projectDtos = new ArrayList<>();

    Page<ProjectEntity> projectEntityPage;
    try{
      projectEntityPage = projectListRepository.findByUsers(user, pageable);
      System.out.println(projectEntityPage.getContent());
      if (projectEntityPage.isEmpty()){
        return projectDtos;
      }
      projectEntities = projectEntityPage.getContent();

      for(ProjectEntity projectEntity: projectEntities){
        System.out.println(projectEntity.getId());
        List<SelectUserDto> userDtos = new ArrayList<>();
        for(UserEntity userEntity: projectEntity.getUsers()){
          SelectUserDto userDto = new SelectUserDto(userEntity);
          userDtos.add(userDto);
        }
        SelectProjectDto selectProjectDetailDto = new SelectProjectDto(projectEntity, userDtos);
        projectDtos.add(selectProjectDetailDto);
      }

      return projectDtos;
    }catch (Exception e){
      e.printStackTrace();
      throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
    }
  }

}
