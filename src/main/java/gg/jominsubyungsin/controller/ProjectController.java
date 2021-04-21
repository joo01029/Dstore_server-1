package gg.jominsubyungsin.controller;

import gg.jominsubyungsin.domain.dto.file.FileDto;
import gg.jominsubyungsin.domain.dto.project.GetProjectDto;
import gg.jominsubyungsin.domain.dto.query.SelectProjectDto;
import gg.jominsubyungsin.domain.entity.FileEntity;
import gg.jominsubyungsin.domain.entity.ProjectEntity;
import gg.jominsubyungsin.domain.entity.UserEntity;
import gg.jominsubyungsin.response.Response;
import gg.jominsubyungsin.response.projects.GetProjectResponse;
import gg.jominsubyungsin.service.file.FileService;
import gg.jominsubyungsin.service.jwt.JwtService;
import gg.jominsubyungsin.service.multipart.MultipartService;
import gg.jominsubyungsin.service.project.ProjectService;
import gg.jominsubyungsin.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.List;

@Controller
@ResponseBody
@RequestMapping("/project")
public class ProjectController {
  @Autowired
  ProjectService projectService;
  @Autowired
  UserService userService;
  @Autowired
  MultipartService multipartService;
  @Autowired
  JwtService jwtService;
  @Autowired
  FileService fileService;

  @PostMapping("/create")
  public Response createProject(@ModelAttribute GetProjectDto projectDto, @RequestHeader String Authorization){
    Response response = new Response();
    List<UserEntity> userEntities = new ArrayList<>();

    if(Authorization.isEmpty()){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "토큰 없음");
    }
    if(projectDto.getFiles().isEmpty()){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "파일은 무조건 1개 이상 보내야 합니다");
    }
    String email = jwtService.getAccessTokenSubject(Authorization);
    UserEntity mainUser;
    try {
      mainUser = userService.findUser(email);
    }catch (Exception e){
      throw e;
    }
    userEntities.add(mainUser);
    for(Long id:projectDto.getUsers()){
      try{
        UserEntity user = userService.findUserId(id);
        for(UserEntity compare:userEntities) {
          if (user.getId().equals(compare.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "같은 유저 입니다");
          }
        }
        userEntities.add(user);
      }catch (Exception e){
        throw e;
      }
    }
    List<FileDto> files;

    try{
      files = multipartService.uploadMulti(projectDto.getFiles());
    }catch (Exception e){
      throw e;
    }
    List<FileEntity> fileEntities = fileService.createFile(files);


    ProjectEntity projectEntity = projectDto.toEntity(userEntities,fileEntities);

    try{
      projectService.saveProject(projectEntity);
    }catch (Exception e){
      throw e;
    }

    response.setHttpStatus(HttpStatus.OK);
    response.setMessage("프로젝트 저장 성공");
    return response;
  }
  @GetMapping("/list")
  public GetProjectResponse projectList(Pageable pageable){
    GetProjectResponse response = new GetProjectResponse();
    List<SelectProjectDto> projects;

    try{
      projects = projectService.getProjects(pageable);
    }catch (Exception e){
      throw e;
    }

    response.setHttpStatus(HttpStatus.OK);
    response.setMessage("성공");
    response.setProjectList(projects);
    return response;
  }
}
