package gg.jominsubyungsin.controller;

import gg.jominsubyungsin.domain.dto.project.request.GetProjectDto;
import gg.jominsubyungsin.domain.dto.project.dataIgnore.ProjectDto;
import gg.jominsubyungsin.domain.dto.project.dataIgnore.SelectProjectDto;
import gg.jominsubyungsin.domain.dto.project.request.PutProjectDto;

import gg.jominsubyungsin.domain.entity.UserEntity;

import gg.jominsubyungsin.domain.response.Response;
import gg.jominsubyungsin.domain.dto.project.response.GetProjectDetailResponse;
import gg.jominsubyungsin.domain.dto.project.response.GetProjectResponse;

import gg.jominsubyungsin.lib.Log;
import gg.jominsubyungsin.lib.PageEnd;
import gg.jominsubyungsin.service.project.ProjectService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.data.domain.Pageable;
import org.springframework.web.client.HttpServerErrorException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/project")
public class ProjectController {
	private final ProjectService projectService;
	private final PageEnd pageEnd;
	private final Log log;

	/*
	 *프로젝트 생성
	 */
	@PostMapping
	public Response createProject(@ModelAttribute GetProjectDto projectDto, HttpServletRequest request) {
		Response response = new Response();

		try {
			UserEntity mainUser = (UserEntity) request.getAttribute("user");
			if (mainUser == null) {
				throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "토큰이 필요함");
			}
			projectService.saveProject(projectDto, mainUser);
			response.setHttpStatus(HttpStatus.OK);
			response.setMessage("프로젝트 저장 성공");
			return response;
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	/*
	 *프로젝트 리스트 받기
	 */
	@GetMapping
	public GetProjectResponse projectList(Pageable pageable, HttpServletRequest request) {
		GetProjectResponse response = new GetProjectResponse();
		List<SelectProjectDto> projects;
		Long projectNumber;
		UserEntity user = (UserEntity) request.getAttribute("user");
		try {
			projects = projectService.getProjects(pageable, user);
			projectNumber = projectService.countProject();

			Boolean end = pageEnd.pageEnd(pageable.getPageSize(), pageable.getPageNumber(), projectNumber);

			response.setHttpStatus(HttpStatus.OK);
			response.setMessage("성공");
			response.setProjectList(projects);
			response.setEnd(end);
			return response;
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	/*
	 *프로젝트 상세 페이지
	 */
	@GetMapping("/{projectId}")
	public GetProjectDetailResponse projectDetail(HttpServletRequest request, @PathVariable("projectId") Long id) {
		GetProjectDetailResponse response = new GetProjectDetailResponse();

		UserEntity user = (UserEntity) request.getAttribute("user");
		try {
			ProjectDto project = projectService.projectDetail(id, user);

			response.setHttpStatus(HttpStatus.OK);
			response.setMessage("성공");
			response.setProject(project);
			return response;
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	@PutMapping("{projectId}")
	public Response projectUpdate(HttpServletRequest request, @ModelAttribute PutProjectDto putProjectDto, @PathVariable("projectId") Long id) {
		Response response = new Response();

		UserEntity user = (UserEntity) request.getAttribute("user");
		if (user == null) {
			throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "토큰이 필요함");
		}
		try {
			projectService.projectUpdate(id, user, putProjectDto);

			response.setHttpStatus(HttpStatus.OK);
			response.setMessage("성공");
			return response;
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	@DeleteMapping("/{projectId}")
	public Response projectDelete(HttpServletRequest request, @PathVariable("projectId") Long id) {
		Response response = new Response();

		UserEntity user = (UserEntity) request.getAttribute("user");
		if (user == null) {
			throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "토큰이 필요함");
		}
		try {
			projectService.deleteProject(id, user);

			response.setHttpStatus(HttpStatus.OK);
			response.setMessage("성공");
			return response;
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

}
