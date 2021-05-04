package gg.jominsubyungsin.controller;

import gg.jominsubyungsin.domain.dto.comment.GetCommentDto;
import gg.jominsubyungsin.domain.dto.query.SelectCommentDto;
import gg.jominsubyungsin.domain.entity.CommentEntity;
import gg.jominsubyungsin.domain.entity.UserEntity;
import gg.jominsubyungsin.domain.response.Response;
import gg.jominsubyungsin.domain.response.comment.GetCommentResponse;
import gg.jominsubyungsin.service.comment.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@ResponseBody
@RequestMapping("/comment")
public class CommentController {
	@Autowired
	CommentService commentService;

	/*
	*댓글 작성
	 */
	@PostMapping("/create")
	public Response makeComment(HttpServletRequest request, @RequestBody GetCommentDto commentDto){
		Response response = new Response();

		UserEntity user = (UserEntity) request.getAttribute("user");
		try{
			commentService.createComment(commentDto.getComment(), commentDto.getProjectId(),user);

			response.setHttpStatus(HttpStatus.OK);
			response.setMessage("성공");
			return response;
		}catch (Exception e){
			throw e;
		}
	}
	/*
	*댓글 리스트
	 */
	@GetMapping("/list/{id}")
	public GetCommentResponse GetCommentList(Pageable pageable, @PathVariable Long id, HttpServletRequest request){
		GetCommentResponse response = new GetCommentResponse();
		UserEntity user = (UserEntity) request.getAttribute("user");
		try{
			List<SelectCommentDto> comments = commentService.getCommentList(id, pageable, user);
			Long commentNum = commentService.commentNum(id);

			response.setEnd(false);
			if((long) (pageable.getPageNumber() + 1) *pageable.getPageSize() >= commentNum){
				response.setEnd(true);
			}
			response.setComments(comments);
			response.setMessage("성공");
			response.setHttpStatus(HttpStatus.OK);
			return response;
		}catch (Exception e){
			throw e;
		}
	}

}
