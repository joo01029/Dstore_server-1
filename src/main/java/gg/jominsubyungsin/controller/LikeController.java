package gg.jominsubyungsin.controller;

import gg.jominsubyungsin.domain.dto.like.dataIgnore.SelectLikeDto;
import gg.jominsubyungsin.domain.dto.user.dataIgnore.SelectUserDto;
import gg.jominsubyungsin.domain.entity.UserEntity;
import gg.jominsubyungsin.domain.response.Response;
import gg.jominsubyungsin.domain.dto.like.response.GetLikeListResponse;
import gg.jominsubyungsin.lib.PageEnd;
import gg.jominsubyungsin.service.like.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/like")
public class LikeController {
	private final LikeService likeService;
	private final PageEnd pageEnd;
	/*
	 *좋아요 누른 사람 리스트
	 */
	@GetMapping("/user/list/{id}")
	public GetLikeListResponse whoClickLikeUserList(Pageable pageable, @PathVariable Long id, HttpServletRequest request) {
		GetLikeListResponse response = new GetLikeListResponse();
		UserEntity user = (UserEntity) request.getAttribute("user");
		try {
			List<SelectUserDto> users = likeService.getUserList(id, pageable, user);
			Long likeNum = likeService.LikeNum(id);

			Boolean end = pageEnd.pageEnd(pageable.getPageSize(), pageable.getPageNumber(), likeNum);

			response.setEnd(end);
			response.setUsers(users);
			response.setHttpStatus(HttpStatus.OK);
			response.setMessage("성공");
			return response;
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 *좋아요 상태 변경
	 */
	@PutMapping("/change/state/{id}")
	public Response changeLikeStateProject(HttpServletRequest request, @PathVariable("id") Long id) {
		Response response = new Response();

		UserEntity user = (UserEntity) request.getAttribute("user");
		if(user == null){
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "토큰이 필요함");
		}
		try {
			likeService.changeLikeState(id, user);

			response.setHttpStatus(HttpStatus.OK);
			response.setMessage("성공");
			return response;
		} catch (Exception e) {
			throw e;
		}
	}
}
