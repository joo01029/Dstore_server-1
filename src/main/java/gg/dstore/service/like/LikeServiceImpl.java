package gg.dstore.service.like;

import gg.dstore.domain.dto.user.dataIgnore.SelectUserDto;
import gg.dstore.domain.entity.LikeEntity;
import gg.dstore.domain.entity.ProjectEntity;
import gg.dstore.domain.entity.UserEntity;
import gg.dstore.domain.repository.LikeListRepository;
import gg.dstore.domain.repository.LikeRepository;
import gg.dstore.domain.repository.ProjectRepository;
import gg.dstore.service.follow.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class LikeServiceImpl implements LikeService {
	private final FollowService followService;

	private final ProjectRepository projectRepository;
	private final LikeListRepository likeListRepository;
	private final LikeRepository likeRepository;

	@Override
	@Transactional(readOnly = true)
	public List<SelectUserDto> getUserList(Long id, Pageable pageable, UserEntity me) {

		try {
			List<SelectUserDto> likes = new ArrayList<>();
			ProjectEntity project = projectRepository.findById(id).orElseGet(() -> {
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 프로젝트");
			});
			Page<LikeEntity> likeList = likeListRepository.findByProjectAndState(project, true, pageable);


			for (LikeEntity likeEntity : likeList) {
				Boolean follow = followService.followState(likeEntity.getUser(), me);
				likes.add(new SelectUserDto(likeEntity.getUser(), follow));
			}
			return likes;
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * 게시글 좋아요
	 */
	@Override
	@Transactional
	public void changeLikeState(Long id, UserEntity user) {
		try {
			ProjectEntity project = projectRepository.findByIdAndOnDelete(id, false).orElseGet(() -> {
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 게시글");
			});

			LikeEntity likeEntity = likeRepository.findByProjectAndUser(project, user)
					.orElse(new LikeEntity(project, user, false));
			user.getLikes().remove(likeEntity);

			likeEntity.setState(!likeEntity.getState());
			project.add(likeEntity);
			user.add(likeEntity);

			likeRepository.save(likeEntity);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Long LikeNum(Long id) {
		try {
			ProjectEntity project = projectRepository.findByIdAndOnDelete(id, false).orElseGet(() -> {
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 게시글");
			});

			return likeRepository.countByProjectAndState(project, true);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public LikeEntity getLikeState(ProjectEntity project, UserEntity user) {
		try {
			return likeRepository.findByProjectAndUser(project, user).orElse(
					new LikeEntity(project, user, false)
			);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional
	public void setLikeFalse(LikeEntity like) {
		try {
			like.setState(false);
			likeRepository.save(like);
		}catch (Exception e){
			throw e;
		}
	}
}
