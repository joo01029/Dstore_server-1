package gg.dstore.domain.dto.user.dataIgnore;

import gg.dstore.domain.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelectUserDto {
	private Long id;
	private String name;
	private String profileImage;
	private Boolean follow;

	public SelectUserDto(UserEntity userEntity,Boolean follow) {
		this.id = userEntity.getId();
		this.name = userEntity.getName();
		this.profileImage = userEntity.getProfileImage();
		this.follow = follow;
	}
}
