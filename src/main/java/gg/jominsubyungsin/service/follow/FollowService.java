package gg.jominsubyungsin.service.follow;

import gg.jominsubyungsin.domain.dto.user.dataIgnore.SelectUserDto;
import gg.jominsubyungsin.domain.entity.FollowEntity;
import gg.jominsubyungsin.domain.entity.UserEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FollowService {
	Long countFollower(Long userId);

	Long countFollowing(Long userId);

	Boolean followState(UserEntity following, UserEntity user);

	void ChangeFollowState(UserEntity follower, Long followingId);

	List<SelectUserDto> showFollower(Long userId, UserEntity user, Pageable pageable);

	List<SelectUserDto> showFollowing(Long userId, UserEntity user, Pageable pageable);

	void setFollowFalse(FollowEntity follow);
}
