package gg.jominsubyungsin.service.follow;

import gg.jominsubyungsin.domain.dto.user.dataIgnore.SelectUserDto;
import gg.jominsubyungsin.domain.entity.FollowEntity;
import gg.jominsubyungsin.domain.entity.UserEntity;
import gg.jominsubyungsin.domain.repository.FollowListRepository;
import gg.jominsubyungsin.domain.repository.FollowRepository;
import gg.jominsubyungsin.domain.repository.UserRepository;
import gg.jominsubyungsin.lib.Log;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {
	private final FollowRepository followRepository;
	private final UserRepository userRepository;
	private final FollowListRepository followListRepository;
	private final Log log;
	@Override
	@Transactional(readOnly = true)
	public Long countFollower(Long userId) {
		try {
			UserEntity user = userRepository.findById(userId).orElseGet(() -> {
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 유저");
			});
			return followRepository.countByFollowingAndFollowState(user, true);
		} catch (HttpClientErrorException e) {
			log.error("count follower number");
			throw e;
		} catch (Exception e) {
			log.error("count follower number");
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Long countFollowing(Long userId) {
		try {
			UserEntity user = userRepository.findById(userId).orElseGet(() -> {
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 유저");
			});
			return followRepository.countByFollowerAndFollowState(user, true);
		} catch (HttpClientErrorException e) {
			log.error("count following number");
			throw e;
		} catch (Exception e) {
			log.error("count following number");
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Boolean followState(UserEntity following, UserEntity user) {
		try {
			Optional<FollowEntity> follow = followRepository.findByFollowerAndFollowingAndFollowState(user, following, true);
			return follow.isPresent();
		} catch (Exception e) {
			log.error("get follow state error");
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
			log.error("change follow state error");
			throw e;
		} catch (Exception e) {
			log.error("change follow state error");
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	@Override
	@Transactional(readOnly = true)
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
			log.error("show followers error");
			throw e;
		} catch (Exception e) {
			log.error("show followers error");
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	@Override
	@Transactional(readOnly = true)
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
			log.error("show followings error");
			throw e;
		} catch (Exception e) {
			log.error("show followings error");
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	@Override
	@Transactional
	public void setFollowFalse(FollowEntity follow) {
		try {
			follow.setFollowState(false);
			followRepository.save(follow);
		}catch (Exception e){
			log.error("set followState false error");
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}
}
