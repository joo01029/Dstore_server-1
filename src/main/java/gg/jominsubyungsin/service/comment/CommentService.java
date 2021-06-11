package gg.jominsubyungsin.service.comment;

import gg.jominsubyungsin.domain.dto.comment.dataIgnore.SelectCommentDto;
import gg.jominsubyungsin.domain.entity.UserEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {
	void createComment(String comment, Long id, UserEntity user);

	List<SelectCommentDto> getCommentList(Long id, Pageable pageable, UserEntity me);

	Long commentNum(Long id);

	void deleteComment(Long id, UserEntity user);
}
