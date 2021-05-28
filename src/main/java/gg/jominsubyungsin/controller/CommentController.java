package gg.jominsubyungsin.controller;

import gg.jominsubyungsin.domain.dto.comment.request.GetCommentDto;
import gg.jominsubyungsin.domain.dto.comment.dataIgnore.SelectCommentDto;
import gg.jominsubyungsin.domain.entity.UserEntity;
import gg.jominsubyungsin.domain.response.Response;
import gg.jominsubyungsin.domain.dto.comment.response.GetCommentResponse;
import gg.jominsubyungsin.lib.PageEnd;
import gg.jominsubyungsin.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comment")
public class CommentController {
	private final CommentService commentService;
	private final PageEnd pageEnd;
	/*
	 *댓글 작성
	 */

	@PostMapping
	public Response makeComment(HttpServletRequest request, @RequestBody GetCommentDto commentDto) {
		Response response = new Response();

		UserEntity user = (UserEntity) request.getAttribute("user");
		if(user == null){
			throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "토큰이 필요합니다.");
		}
		try {
			commentService.createComment(commentDto.getComment(), commentDto.getProjectId(), user);

			response.setHttpStatus(HttpStatus.OK);
			response.setMessage("성공");
			return response;
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 *댓글 리스트
	 */
	@GetMapping("/{projectId}")
	public GetCommentResponse GetCommentList(Pageable pageable, @PathVariable Long projectId, HttpServletRequest request) {
		GetCommentResponse response = new GetCommentResponse();
		UserEntity user = (UserEntity) request.getAttribute("user");
		try {
			List<SelectCommentDto> comments = commentService.getCommentList(projectId, pageable, user);
			Long commentNum = commentService.commentNum(projectId);

			Boolean end = pageEnd.pageEnd(pageable.getPageSize(), pageable.getPageNumber(), commentNum);

			response.setEnd(end);
			response.setComments(comments);
			response.setMessage("성공");
			response.setHttpStatus(HttpStatus.OK);
			return response;
		} catch (Exception e) {
			throw e;
		}
	}

	@DeleteMapping("/{commentId}")
	public Response deleteComment(@PathVariable Long commentId, HttpServletRequest request){
		Response response = new Response();
		UserEntity user = (UserEntity) request.getAttribute("user");
		if(user == null){
			throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "토큰이 필요함");
		}
		try{
			commentService.deleteComement(commentId, user);

			response.setHttpStatus(HttpStatus.OK);
			response.setMessage("성공");
			return response;
		}catch (Exception e){
			throw e;
		}
	}

}
