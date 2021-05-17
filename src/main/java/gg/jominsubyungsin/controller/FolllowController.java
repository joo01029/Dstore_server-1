package gg.jominsubyungsin.controller;

import gg.jominsubyungsin.domain.dto.user.dataIgnore.SelectUserDto;
import gg.jominsubyungsin.domain.entity.UserEntity;
import gg.jominsubyungsin.domain.dto.user.response.ShowUserListResponse;
import gg.jominsubyungsin.lib.PageEnd;
import gg.jominsubyungsin.service.follow.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/follow")
public class FolllowController {
	private final FollowService followService;
	private final PageEnd pageEnd;
	/*
	 * 팔로워 리스트
	 */
	@GetMapping("/follower/list/{id}")
	public ShowUserListResponse showFollowers(HttpServletRequest request, @PathVariable Long id, Pageable pageable) {
		ShowUserListResponse response = new ShowUserListResponse();
		UserEntity user = (UserEntity) request.getAttribute("user");
		try {
			List<SelectUserDto> users = followService.showFollower(id, user, pageable);
			response.setUserList(users);
			Long followers = followService.countFollower(id);

			response.setEnd(false);
			if (followers <= (long) (pageable.getPageNumber() + 1) * pageable.getPageSize()) {
				response.setEnd(true);
			}
			response.setHttpStatus(HttpStatus.OK);
			response.setMessage("성공");

			return response;
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 *팔로잉 리스트
	 */
	@GetMapping("/following/list/{id}")
	public ShowUserListResponse showFollowing(HttpServletRequest request, @PathVariable Long id, Pageable pageable) {
		ShowUserListResponse response = new ShowUserListResponse();
		UserEntity user = (UserEntity) request.getAttribute("user");
		try {
			List<SelectUserDto> users = followService.showFollowing(id, user, pageable);
			response.setUserList(users);
			Long followings = followService.countFollowing(id);

			Boolean end = pageEnd.pageEnd(pageable.getPageSize(), pageable.getPageNumber(), followings);

			response.setEnd(end);
			response.setHttpStatus(HttpStatus.OK);
			response.setMessage("성공");

			return response;
		} catch (Exception e) {
			throw e;
		}
	}
}
