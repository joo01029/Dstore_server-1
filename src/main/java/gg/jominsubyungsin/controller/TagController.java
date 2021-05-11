package gg.jominsubyungsin.controller;

import gg.jominsubyungsin.domain.dto.project.dataIgnore.SelectProjectDto;
import gg.jominsubyungsin.domain.dto.project.response.GetProjectResponse;
import gg.jominsubyungsin.domain.dto.tag.response.FindTagResponse;
import gg.jominsubyungsin.domain.entity.UserEntity;
import gg.jominsubyungsin.service.tag.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@ResponseBody
@RequestMapping("/tag")
public class TagController {
	@Autowired
	TagService tagService;

	@GetMapping("/find/like/{tag}")
	public FindTagResponse findTagLike(@PathVariable String tag, Pageable pageable){
		FindTagResponse response = new FindTagResponse();
		try{
			List<String> tags = tagService.TagList(tag, pageable);

			response.setTags(tags);
			response.setHttpStatus(HttpStatus.OK);
			response.setMessage("성공");
			return response;
		}catch (Exception e){
			throw e;
		}
	}
	@GetMapping("/find/tags")
	public GetProjectResponse findByTags(@RequestParam List<String> tags, HttpServletRequest request, Pageable pageable){
		GetProjectResponse response = new GetProjectResponse();

		UserEntity user = (UserEntity) request.getAttribute("user");
		try{
			List<SelectProjectDto> projectList = tagService.projectList(tags, user, pageable);
			Long projectnumber = tagService.projectListCount(tags);

			response.setEnd(false);
			if((long) (pageable.getPageNumber() + 1) *pageable.getPageSize() >= projectnumber){
				response.setEnd(true);
			}
			response.setProjectList(projectList);
			response.setHttpStatus(HttpStatus.OK);
			response.setMessage("성공");
			return response;
		}catch (Exception e){
			throw e;
		}

	}
}
