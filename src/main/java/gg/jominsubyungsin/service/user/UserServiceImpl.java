package gg.jominsubyungsin.service.user;

import gg.jominsubyungsin.domain.dto.project.dataIgnore.SelectProjectDto;
import gg.jominsubyungsin.domain.dto.user.dataIgnore.SelectUserDto;
import gg.jominsubyungsin.domain.dto.user.request.UserDto;
import gg.jominsubyungsin.domain.dto.user.request.UserUpdateDto;
import gg.jominsubyungsin.domain.dto.user.response.UserDetailResponseDto;
import gg.jominsubyungsin.domain.entity.*;
import gg.jominsubyungsin.domain.repository.ProjectUserConnectRepository;
import gg.jominsubyungsin.domain.repository.UserRepository;
import gg.jominsubyungsin.service.comment.CommentService;
import gg.jominsubyungsin.service.follow.FollowService;
import gg.jominsubyungsin.service.like.LikeService;
import gg.jominsubyungsin.service.project.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
	private final FollowService followService;
	private final ProjectUserConnectRepository projectUserConnectRepository;
	private final UserRepository userRepository;
	private final CommentService commentService;
	private final LikeService likeService;

	@Autowired
	@Lazy
	private ProjectService projectService;

	@Override
	@Transactional
	public boolean userUpdate(UserUpdateDto userUpdateDto) throws HttpServerErrorException {
		try {
			String changePassword = userUpdateDto.getChangePassword();
			String changeName = userUpdateDto.getChangeName();

			return userRepository.findByEmailAndPasswordAndOnDelete(userUpdateDto.getEmail(), userUpdateDto.getPassword(), false)
					.map(found -> {
						found.setPassword(Optional.ofNullable(changePassword).orElse(found.getPassword()));
						found.setName(Optional.ofNullable(changeName).orElse(found.getName()));
						userRepository.save(found);
						return true;
					}).orElse(false);
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	@Override
	@Transactional
	public boolean userUpdateIntroduce(UserDto userDto) {
		String introduce = userDto.getIntroduce();
		try {
			return userRepository.findByEmailAndOnDelete(userDto.getEmail(), false)
					.map(found -> {
						found.setIntroduce(introduce);
						userRepository.save(found);
						return true;
					}).orElse(false);
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	@Override
	@Transactional
	public boolean userDelete(UserDto userDto) {
		try {
			UserEntity findUser = userRepository.findByEmailAndPasswordAndOnDelete(userDto.getEmail(), userDto.getPassword(), false).orElseGet(() -> {
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "유저가 존재하지 않음");
			});

			findUser.setOnDelete(true);
			//댓글 삭제

			for (CommentEntity comment : findUser.getComments()) {
				commentService.deleteComement(comment.getId(), findUser);
			}
			//팔로워 없애기
			for (FollowEntity follow : findUser.getFollower()) {
				followService.setFollowFalse(follow);
			}
			//팔로윙 없애기
			for (FollowEntity follow : findUser.getFollowing()) {
				followService.setFollowFalse(follow);
			}
			//좋아요 삭제
			for (LikeEntity like : findUser.getLikes()){
				likeService.setLikeFalse(like);
			}

			for(ProjectUserConnectEntity connect : findUser.getProjects()){
				connect.setGetOut(true);
				projectUserConnectRepository.save(connect);
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public SelectUserDto findUser(Long id, UserEntity user) {
		try {
			UserEntity userEntity = userRepository.findByIdAndOnDelete(id, false).orElseGet(() -> {
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 유저");
			});
			return new SelectUserDto(userEntity, followService.followState(userEntity, user));
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public UserEntity findUserById(Long id) {
		try {
			Optional<UserEntity> findUser = userRepository.findByIdAndOnDelete(id, false);

			return findUser.orElseGet(() -> {
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "유저가 존재하지 않음");
			});
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public UserEntity findUser(String email) {
		try {
			Optional<UserEntity> findUser = userRepository.findByEmailAndOnDelete(email, false);

			return findUser.orElseGet(() -> {
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "유저가 존재하지 않음");
			});
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<SelectUserDto> findUserLikeName(String name, String email, UserEntity user) {
		try {
			List<UserEntity> findUsers = userRepository.findByNameLike(name, email);
			List<SelectUserDto> userList = new ArrayList<>();

			for (UserEntity userEntity : findUsers) {
				userList.add(new SelectUserDto(userEntity, followService.followState(userEntity, user)));
			}
			return userList;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public boolean checkUserSame(String email, Long id) {
		UserEntity findUser;
		try {
			findUser = userRepository.findByEmailAndOnDelete(email, false).orElseGet(() -> {
				throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "유저 못찾음");
			});
			if (findUser.getId().equals(id))
				return true;
			else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	@Override
	@Transactional
	public void updateProfileImage(String email, String fileUrl) {
		UserEntity findUser;
		try {
			findUser = userRepository.findByEmailAndOnDelete(email, false).orElseGet(() -> {
				throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "유저 못찾음");
			});
			findUser.setProfileImage(fileUrl);
			userRepository.save(findUser);
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetailResponseDto getUserDetail(Long id, UserEntity user, Pageable pageable){
		Boolean myProfile = false;
		UserEntity profile = findUserById(id);;
		if(user != null) {
			myProfile = checkUserSame(user.getEmail(), id);
		}

		List<SelectProjectDto> selectProjectDetailDtos = projectService.getProjects(pageable, user, profile);
		Long follower = followService.countFollower(id);
		Long following = followService.countFollowing(id);
		Boolean follow = followService.followState(profile, user);
		UserDetailResponseDto userDetailResponseDto = new UserDetailResponseDto(profile, myProfile, selectProjectDetailDtos, follower, following, follow);
		return userDetailResponseDto;
	}
}
