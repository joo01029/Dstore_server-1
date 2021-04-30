package gg.jominsubyungsin.domain.dto.query;

import gg.jominsubyungsin.domain.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelectUserDto {
	private Long id;
	private String name;
	private String profileImage;

	public SelectUserDto(UserEntity userEntity) {
		this.id = userEntity.getId();
		this.name = userEntity.getName();
		this.profileImage = userEntity.getProfileImage();
	}
}
