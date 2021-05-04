package gg.jominsubyungsin.service.comment;

import gg.jominsubyungsin.domain.dto.comment.dataIgnore.SelectCommentDto;
import gg.jominsubyungsin.domain.entity.CommentEntity;
import gg.jominsubyungsin.domain.entity.ProjectEntity;
import gg.jominsubyungsin.domain.entity.UserEntity;
import gg.jominsubyungsin.domain.repository.CommentListRepository;
import gg.jominsubyungsin.domain.repository.CommentRepository;
import gg.jominsubyungsin.domain.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{
	@Autowired
	ProjectRepository projectRepository;
	@Autowired
	CommentRepository commentRepository;
	@Autowired
	CommentListRepository commentListRepository;
	@Override
	public void createComment(String comment, Long id, UserEntity user) {
		try {
			ProjectEntity project = projectRepository.findById(id).orElseGet(()->{
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,"존재하지 않는 게시글");
			});
			CommentEntity commentEntity = new CommentEntity(comment,project,user);
			project.add(commentEntity);

			commentRepository.save(commentEntity);
		}catch (Exception e){
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	@Override
	public List<SelectCommentDto> getCommentList(Long id, Pageable pageable, UserEntity me) {
		List<SelectCommentDto> comments = new ArrayList<>();
		try{
			ProjectEntity project = projectRepository.findById(id).orElseGet(()->{
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,"존재하지 않는 게시글");
			});
			Page<CommentEntity> pageComments = commentListRepository.findByProject(project, pageable);
			for(CommentEntity comment:pageComments){
				comments.add(new SelectCommentDto(comment, me));
			}
			return comments;
		}catch (Exception e){
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR,"서버 에러");
		}
	}

	@Override
	public Long commentNum(Long id) {
		try {
			ProjectEntity project = projectRepository.findById(id).orElseGet(() -> {
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 게시글");
			});
			return commentRepository.countByProject(project);
		}catch (Exception e){
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR,"서버 에러");
		}
	}
}
