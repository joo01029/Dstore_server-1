package gg.jominsubyungsin.controller;

import gg.jominsubyungsin.domain.dto.user.dataIgnore.SelectUserDto;
import gg.jominsubyungsin.domain.entity.UserEntity;
import gg.jominsubyungsin.domain.response.Response;
import gg.jominsubyungsin.domain.dto.like.response.GetLikeListResponse;
import gg.jominsubyungsin.lib.Log;
import gg.jominsubyungsin.lib.PageEnd;
import gg.jominsubyungsin.service.like.LikeService;
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
@RequestMapping("/like")
public class LikeController {
	private final LikeService likeService;
	private final PageEnd pageEnd;
	private final Log log;
	/*
	 *좋아요 누른 사람 리스트
	 */
	@GetMapping("/users/{projectId}")
	public GetLikeListResponse getLikeUserList(Pageable pageable, @PathVariable("projectId") Long id, HttpServletRequest request) {
		GetLikeListResponse response = new GetLikeListResponse();
		try {
			UserEntity user = (UserEntity) request.getAttribute("user");
			List<SelectUserDto> users = likeService.getUserList(id, pageable, user);
			Long likeNum = likeService.LikeNum(id);

			Boolean end = pageEnd.pageEnd(pageable.getPageSize(), pageable.getPageNumber(), likeNum);

			response.setEnd(end);
			response.setUsers(users);
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
	 *좋아요 상태 변경
	 */
	@PutMapping("/{projectId}")
	public Response changeLikeStateProject(HttpServletRequest request, @PathVariable("projectId") Long id) {
		Response response = new Response();

		try {
			UserEntity user = (UserEntity) request.getAttribute("user");
			if (user == null) {
				throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "토큰이 필요함");
			}
			likeService.changeLikeState(id, user);

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
