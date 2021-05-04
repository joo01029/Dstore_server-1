package gg.jominsubyungsin.controller;

import gg.jominsubyungsin.domain.dto.like.dataIgnore.SelectLikeDto;
import gg.jominsubyungsin.domain.entity.UserEntity;
import gg.jominsubyungsin.domain.response.Response;
import gg.jominsubyungsin.domain.dto.like.response.GetLikeListResponse;
import gg.jominsubyungsin.service.like.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@ResponseBody
@RequestMapping("/like")
public class LikeController {
	@Autowired
	LikeService likeService;

	/*
	*좋아요 누른 사람 리스트
	 */
	@GetMapping("/user/list/{id}")
	public GetLikeListResponse whoClickLikeUserList(Pageable pageable, @PathVariable Long id, HttpServletRequest request){
		GetLikeListResponse response = new GetLikeListResponse();
		UserEntity user = (UserEntity) request.getAttribute("user");
		try{
			List<SelectLikeDto> likes = likeService.getUserList(id,pageable, user);
			Long likeNum = likeService.LikeNum(id);

			response.setEnd(false);
			if((long) (pageable.getPageNumber() + 1) * pageable.getPageSize() >= likeNum){
				response.setEnd(true);
			}
			response.setLikes(likes);
			response.setHttpStatus(HttpStatus.OK);
			response.setMessage("성공");
			return response;
		}catch (Exception e){
			throw e;
		}
	}

	/*
	*좋아요 상태 변경
	 */
	@PutMapping("/change/state/{id}")
	public Response changeLikeStateProject(HttpServletRequest request, @PathVariable("id") Long id){
		Response response = new Response();

		UserEntity user = (UserEntity) request.getAttribute("user");
		try{
			likeService.changeLikeState(id, user);

			response.setHttpStatus(HttpStatus.OK);
			response.setMessage("성공");
			return response;
		}catch (Exception e){
			throw e;
		}
	}
}
