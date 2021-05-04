package gg.jominsubyungsin.domain.dto.query;

import gg.jominsubyungsin.domain.entity.CommentEntity;
import gg.jominsubyungsin.domain.entity.UserEntity;
import gg.jominsubyungsin.service.follow.FollowService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
@Setter
public class SelectCommentDto {
	@Autowired
	FollowService followService;
	@NotBlank
	private Long id;
	@NotBlank
	private String comment;

	private Date createAt;

	private SelectUserDto user;

	public SelectCommentDto(CommentEntity commentEntity, UserEntity me){
		id = commentEntity.getId();
		comment = commentEntity.getComment();
		createAt = commentEntity.getCreateAt();
		user = new SelectUserDto(commentEntity.getUser(), followService.followState(commentEntity.getUser(), me));
	}
}
