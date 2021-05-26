package gg.jominsubyungsin.domain.dto.comment.dataIgnore;

import gg.jominsubyungsin.domain.dto.user.dataIgnore.SelectUserDto;
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
	@NotBlank
	private Long id;
	@NotBlank
	private String comment;

	private Date createAt;

	private SelectUserDto user;

	public SelectCommentDto(CommentEntity commentEntity, SelectUserDto user){
		id = commentEntity.getId();
		comment = commentEntity.getComment();
		createAt = commentEntity.getCreateAt();
		this.user = user;
	}
}
