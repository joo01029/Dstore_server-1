package gg.dstore.domain.dto.comment.dataIgnore;

import gg.dstore.domain.dto.user.dataIgnore.SelectUserDto;
import gg.dstore.domain.entity.CommentEntity;
import lombok.Getter;
import lombok.Setter;

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
