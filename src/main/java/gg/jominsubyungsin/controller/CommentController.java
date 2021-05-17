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
	@PostMapping("/create")
	public Response makeComment(HttpServletRequest request, @RequestBody GetCommentDto commentDto) {
		Response response = new Response();

		UserEntity user = (UserEntity) request.getAttribute("user");
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
	@GetMapping("/list/{id}")
	public GetCommentResponse GetCommentList(Pageable pageable, @PathVariable Long id, HttpServletRequest request) {
		GetCommentResponse response = new GetCommentResponse();
		UserEntity user = (UserEntity) request.getAttribute("user");
		try {
			List<SelectCommentDto> comments = commentService.getCommentList(id, pageable, user);
			Long commentNum = commentService.commentNum(id);

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

}
