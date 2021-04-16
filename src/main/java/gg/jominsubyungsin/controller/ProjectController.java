package gg.jominsubyungsin.controller;

import gg.jominsubyungsin.domain.dto.file.FileDto;
import gg.jominsubyungsin.domain.dto.project.ReturnProjectDto;
import gg.jominsubyungsin.domain.entity.FileEntity;
import gg.jominsubyungsin.domain.entity.ProjectEntity;
import gg.jominsubyungsin.domain.entity.UserEntity;
import gg.jominsubyungsin.response.Response;
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
  public Response createProject(@ModelAttribute ReturnProjectDto projectDto, @RequestHeader String Authorization){
    Response response = new Response();
    List<UserEntity> userEntities = new ArrayList<>();

    if(Authorization.isEmpty()){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "토큰 없음");
    }
    String email = jwtService.getAccessTokenSubject(Authorization);
    userEntities.add(userService.findUser(email));
    for(Long id:projectDto.getUsers()){
      try{
        userEntities.add(userService.findUserId(id));
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
    response.setResult(true);
    response.setStatus(HttpStatus.OK.value());
    response.setMessage("프로젝트 저장 성공");
    return response;
  }
}
