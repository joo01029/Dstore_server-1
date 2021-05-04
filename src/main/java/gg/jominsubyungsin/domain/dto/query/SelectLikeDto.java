package gg.jominsubyungsin.domain.dto.query;

import gg.jominsubyungsin.domain.entity.LikeEntity;
import gg.jominsubyungsin.domain.entity.UserEntity;
import gg.jominsubyungsin.service.follow.FollowService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

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
