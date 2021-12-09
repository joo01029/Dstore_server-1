package gg.dstore.service.follow;

import gg.dstore.domain.dto.user.dataIgnore.SelectUserDto;
import gg.dstore.domain.entity.FollowEntity;
import gg.dstore.domain.entity.UserEntity;
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
