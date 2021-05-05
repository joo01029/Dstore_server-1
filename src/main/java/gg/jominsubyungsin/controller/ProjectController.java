package gg.jominsubyungsin.controller;

import gg.jominsubyungsin.domain.dto.file.request.FileDto;
import gg.jominsubyungsin.domain.dto.project.request.GetProjectDto;
import gg.jominsubyungsin.domain.dto.project.dataIgnore.ProjectDto;
import gg.jominsubyungsin.domain.dto.project.dataIgnore.SelectProjectDto;
import gg.jominsubyungsin.domain.entity.FileEntity;
import gg.jominsubyungsin.domain.entity.ProjectEntity;
import gg.jominsubyungsin.domain.entity.TagEntity;
import gg.jominsubyungsin.domain.entity.UserEntity;
import gg.jominsubyungsin.domain.response.Response;
import gg.jominsubyungsin.domain.dto.project.response.GetProjectDetailResponse;
import gg.jominsubyungsin.domain.dto.project.response.GetProjectResponse;
import gg.jominsubyungsin.service.file.FileService;
import gg.jominsubyungsin.service.jwt.JwtService;
import gg.jominsubyungsin.service.multipart.MultipartService;
import gg.jominsubyungsin.service.project.ProjectService;
import gg.jominsubyungsin.service.tag.TagService;
import gg.jominsubyungsin.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.HTML;
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
	@Autowired
	TagService tagService;

	/*
	프로젝트 생성
	 */
	@PostMapping("/create")
	public Response createProject(@ModelAttribute GetProjectDto projectDto, HttpServletRequest request) {
		Response response = new Response();

		List<UserEntity> userEntities = new ArrayList<>();
		if (projectDto.getFiles().isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "파일은 무조건 1개 이상 보내야 합니다");
		}

		UserEntity user = (UserEntity) request.getAttribute("user");
		try {
			UserEntity mainUser = userService.findUser(user.getEmail());
			userEntities.add(mainUser);
			//id로 유저 찾기
			for (Long id : projectDto.getUsers()) {
				UserEntity saveUser = userService.findUserById(id);
				for (UserEntity compare : userEntities) {
					if (saveUser.equals(compare)) {
						throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "같은 유저 입니다");
					}
				}
				userEntities.add(saveUser);
			}
			//파일 업로드
			List<FileDto> files = multipartService.uploadMulti(projectDto.getFiles());
			List<FileEntity> fileEntities = fileService.createFiles(files);

			List<TagEntity> tags = tagService.createTag(projectDto.getTags());

			ProjectEntity projectEntity = projectDto.toEntity(userEntities, fileEntities, tags);

			projectService.saveProject(projectEntity);
		} catch (Exception e) {
			throw e;
		}

		response.setHttpStatus(HttpStatus.OK);
		response.setMessage("프로젝트 저장 성공");
		return response;
	}

	/*
	프로젝트 리스트 받기
	 */
	@GetMapping("/list")
	public GetProjectResponse projectList(Pageable pageable, HttpServletRequest request) {
		GetProjectResponse response = new GetProjectResponse();
		List<SelectProjectDto> projects;
		Long projectNumber;
		UserEntity user = (UserEntity) request.getAttribute("user");
		try {
			projects = projectService.getProjects(pageable, user);
			projectNumber = projectService.countProject();
		} catch (Exception e) {
			throw e;
		}

		Boolean end = projectNumber <= (long) pageable.getPageSize() *(pageable.getPageNumber()+1);

		response.setHttpStatus(HttpStatus.OK);
		response.setMessage("성공");
		response.setProjectList(projects);
		response.setEnd(end);
		return response;
	}

	/*
	프로젝트 상세 페이지
	 */
	@GetMapping("/detail/{id}")
	public GetProjectDetailResponse projectDetail(HttpServletRequest request, @PathVariable("id") Long id) {
		GetProjectDetailResponse response = new GetProjectDetailResponse();

		UserEntity user = (UserEntity) request.getAttribute("user");
		try {
			ProjectDto project = projectService.projectDetail(id, user);

			response.setHttpStatus(HttpStatus.OK);
			response.setMessage("성공");
			response.setProject(project);
			return response;
		} catch (Exception e) {
			throw e;
		}
	}
}
