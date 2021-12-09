package gg.dstore.domain.dto.like.dataIgnore;

import gg.dstore.domain.dto.user.dataIgnore.SelectUserDto;
import gg.dstore.domain.entity.LikeEntity;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
@Getter
@Setter
public class SelectLikeDto {
	@NotBlank
	private Long id;

	private SelectUserDto user;
	public SelectLikeDto(LikeEntity like, Boolean follow){
		this.id = like.getId();
		System.out.println(like.getUser().toString());
		this.user = new SelectUserDto(like.getUser(), follow);
	}
}
