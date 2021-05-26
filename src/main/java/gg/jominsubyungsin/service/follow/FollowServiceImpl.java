package gg.jominsubyungsin.service.follow;

import gg.jominsubyungsin.domain.dto.user.dataIgnore.SelectUserDto;
import gg.jominsubyungsin.domain.entity.FollowEntity;
import gg.jominsubyungsin.domain.entity.UserEntity;
import gg.jominsubyungsin.domain.repository.FollowListRepository;
import gg.jominsubyungsin.domain.repository.FollowRepository;
import gg.jominsubyungsin.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FollowServiceImpl implements FollowService {
	@Autowired
	FollowRepository followRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	FollowListRepository followListRepository;

	@Override
	public Long countFollower(Long userId) {
		try {
			UserEntity user = userRepository.findById(userId).orElseGet(() -> {
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 유저");
			});
			return followRepository.countByFollowingAndFollowState(user, true);
		} catch (HttpClientErrorException e) {
			throw e;
		} catch (Exception e) {
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	@Override
	public Long countFollowing(Long userId) {
		try {
			UserEntity user = userRepository.findById(userId).orElseGet(() -> {
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 유저");
			});
			return followRepository.countByFollowerAndFollowState(user, true);
		} catch (HttpClientErrorException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	@Override
	public Boolean followState(UserEntity following, UserEntity user) {
		try {
			Optional<FollowEntity> follow = followRepository.findByFollowerAndFollowingAndFollowState(user, following, true);
			return follow.isPresent();
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	@Override
	@Transactional
	public void ChangeFollowState(UserEntity follower, Long followingId) {
		try {
			if (follower.getId().equals(followingId))
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "자기자신은 팔로우 불가능");

			UserEntity following = userRepository.findById(followingId).orElseGet(() -> {
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 유저");
			});

			FollowEntity follow = followRepository.findByFollowerAndFollowingAndFollowState(follower, following, true)
					.orElse(new FollowEntity(follower, following, false));
			follower.getFollowing().remove(follow);
			following.getFollower().remove(follow);

			follow.setFollowState(!follow.getFollowState());
			follower.addFollowing(follow);
			following.addFollower(follow);
			followRepository.save(follow);
		} catch (HttpClientErrorException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	@Override
	public List<SelectUserDto> showFollower(Long userId, UserEntity user, Pageable pageable) {
		try {
			UserEntity following = userRepository.findById(userId).orElseGet(() -> {
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 유저");
			});

			Page<FollowEntity> followers = followListRepository.findByFollowingAndFollowState(following, true, pageable);
			List<SelectUserDto> follower = new ArrayList<>();
			for (FollowEntity followEntity : followers) {
				follower.add(new SelectUserDto(followEntity.getFollower(), followState(followEntity.getFollower(), user)));
			}

			return follower;
		} catch (HttpClientErrorException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	@Override
	public List<SelectUserDto> showFollowing(Long userId, UserEntity user, Pageable pageable) {
		try {
			UserEntity follower = userRepository.findById(userId).orElseGet(() -> {
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 유저");
			});

			Page<FollowEntity> followings = followListRepository.findByFollowerAndFollowState(follower, true, pageable);
			List<SelectUserDto> following = new ArrayList<>();
			for (FollowEntity followEntity : followings) {
				following.add(new SelectUserDto(followEntity.getFollower(), followState(followEntity.getFollower(), user)));
			}

			return following;
		} catch (HttpClientErrorException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	@Override
	@Transactional
	public void setFollowFalse(FollowEntity follow) {
		follow.setFollowState(false);
		followRepository.save(follow);
	}
}
