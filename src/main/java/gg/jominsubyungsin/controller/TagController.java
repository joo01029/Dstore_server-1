package gg.jominsubyungsin.controller;

import gg.jominsubyungsin.domain.dto.project.dataIgnore.SelectProjectDto;
import gg.jominsubyungsin.domain.dto.project.response.GetProjectResponse;
import gg.jominsubyungsin.domain.dto.tag.response.FindTagResponse;
import gg.jominsubyungsin.domain.entity.UserEntity;
import gg.jominsubyungsin.lib.Log;
import gg.jominsubyungsin.lib.PageEnd;
import gg.jominsubyungsin.service.tag.TagService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tag")
public class TagController {
	private final TagService tagService;
	private final PageEnd pageEnd;
	private final Log log;

	@GetMapping("/similar/{tag}")
	public FindTagResponse findTagLike(@PathVariable String tag, Pageable pageable) {
		FindTagResponse response = new FindTagResponse();
		try {
			List<String> tags = tagService.TagList(tag, pageable);

			response.setTags(tags);
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

	@GetMapping("/projects")
	public GetProjectResponse findProjectByTags(@RequestParam List<String> tags, HttpServletRequest request, Pageable pageable) {
		GetProjectResponse response = new GetProjectResponse();

		UserEntity user = (UserEntity) request.getAttribute("user");
		try {
			List<SelectProjectDto> projectList = tagService.projectList(tags, user, pageable);
			Long projectnumber = tagService.projectListCount(tags);

			System.out.println(projectnumber);
			Boolean end = pageEnd.pageEnd(pageable.getPageSize(), pageable.getPageNumber(), projectnumber);

			response.setEnd(end);
			response.setProjectList(projectList);
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
