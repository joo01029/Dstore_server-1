package gg.jominsubyungsin.service.comment;

import gg.jominsubyungsin.domain.dto.comment.dataIgnore.SelectCommentDto;
import gg.jominsubyungsin.domain.dto.user.dataIgnore.SelectUserDto;
import gg.jominsubyungsin.domain.entity.CommentEntity;
import gg.jominsubyungsin.domain.entity.ProjectEntity;
import gg.jominsubyungsin.domain.entity.UserEntity;
import gg.jominsubyungsin.domain.repository.CommentListRepository;
import gg.jominsubyungsin.domain.repository.CommentRepository;
import gg.jominsubyungsin.domain.repository.ProjectRepository;
import gg.jominsubyungsin.lib.Log;
import gg.jominsubyungsin.service.follow.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
	private final ProjectRepository projectRepository;
	private final CommentRepository commentRepository;
	private final CommentListRepository commentListRepository;
	private final FollowService followService;
	private final Log log;

	@Override
	@Transactional
	public void createComment(String comment, Long id, UserEntity user) {
		try {
			ProjectEntity project = projectRepository.findByIdAndOnDelete(id, false).orElseGet(() -> {
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 게시글");
			});
			CommentEntity commentEntity = new CommentEntity(comment);
			project.add(commentEntity);
			user.add(commentEntity);
			commentRepository.save(commentEntity);
		} catch (HttpClientErrorException e) {
			log.error("create comment error");
			throw e;
		} catch (Exception e) {
			log.error("create comment error");
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<SelectCommentDto> getCommentList(Long id, Pageable pageable, UserEntity me) {
		List<SelectCommentDto> comments = new ArrayList<>();
		try {
			ProjectEntity project = projectRepository.findByIdAndOnDelete(id, false).orElseGet(() -> {
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 게시글");
			});
			Page<CommentEntity> pageComments = commentListRepository.findByProjectAndOnDelete(project, false, pageable);
			for (CommentEntity comment : pageComments) {
				SelectUserDto userDto = new SelectUserDto(comment.getUser(), followService.followState(comment.getUser(), me));
				comments.add(new SelectCommentDto(comment, userDto));
			}
			return comments;
		} catch (HttpClientErrorException e) {
			log.error("get comment list error");
			throw e;
		} catch (Exception e) {
			log.error("get comment list error");
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	@Override
	public Long commentNum(Long id) {
		try {
			ProjectEntity project = projectRepository.findByIdAndOnDelete(id, false).orElseGet(() -> {
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 게시글");
			});
			return commentRepository.countByProjectAndOnDelete(project, false);
		} catch (HttpClientErrorException e) {
			log.error("count comment number error");
			throw e;
		} catch (Exception e) {
			log.error("count comment number error");
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	@Override
	@Transactional
	public void deleteComement(Long id, UserEntity user) {
		try {
			CommentEntity comment = commentRepository.findByIdAndUser(id, user).orElseGet(() -> {
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 댓글");
			});
			comment.setOnDelete(true);
			commentRepository.save(comment);
		} catch (HttpClientErrorException e) {
			log.error("delete comment error");
			throw e;
		} catch (Exception e) {
			log.error("delete comment error");
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}
}
