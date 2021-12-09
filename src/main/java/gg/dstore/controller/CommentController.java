package gg.dstore.controller;

import gg.dstore.domain.dto.comment.request.GetCommentDto;
import gg.dstore.domain.dto.comment.dataIgnore.SelectCommentDto;
import gg.dstore.domain.entity.UserEntity;
import gg.dstore.domain.response.Response;
import gg.dstore.domain.dto.comment.response.GetCommentResponse;
import gg.dstore.lib.Log;
import gg.dstore.lib.PageEnd;
import gg.dstore.service.comment.CommentService;
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
@RequestMapping("/comment")
public class CommentController {
	private final CommentService commentService;
	private final PageEnd pageEnd;
	private final Log log;
	/*
	 *댓글 작성
	 */

	@PostMapping
	public Response makeComment(HttpServletRequest request, @RequestBody GetCommentDto commentDto) {
		Response response = new Response();
		try {
			UserEntity user = (UserEntity) request.getAttribute("user");
			if (user == null) {
				throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "토큰이 필요합니다.");
			}

			commentService.createComment(commentDto.getComment(), commentDto.getProjectId(), user);

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

	/*
	 *댓글 리스트
	 */
	@GetMapping("/{projectId}")
	public GetCommentResponse getCommentList(Pageable pageable, @PathVariable Long projectId, HttpServletRequest request) {
		GetCommentResponse response = new GetCommentResponse();
		try {
			UserEntity user = (UserEntity) request.getAttribute("user");

			List<SelectCommentDto> comments = commentService.getCommentList(projectId, pageable, user);
			Long commentNum = commentService.commentNum(projectId);

			Boolean end = pageEnd.pageEnd(pageable.getPageSize(), pageable.getPageNumber(), commentNum);

			response.setEnd(end);
			response.setComments(comments);
			response.setMessage("성공");
			response.setHttpStatus(HttpStatus.OK);
			return response;
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	@DeleteMapping("/{commentId}")
	public Response deleteComment(@PathVariable Long commentId, HttpServletRequest request) {
		Response response = new Response();
		try {
			UserEntity user = (UserEntity) request.getAttribute("user");
			if (user == null) {
				throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "토큰이 필요함");
			}
			commentService.deleteComment(commentId, user);

			response.setHttpStatus(HttpStatus.OK);
			response.setMessage("성공");
			return response;
		}catch (HttpClientErrorException | HttpServerErrorException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

}
