package gg.dstore.domain.dto.user.dataIgnore;

import gg.dstore.domain.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelectUserDetailDto {
	private Long id;
	private String name;
	private String introduce;
	private String profileImage;

	public SelectUserDetailDto(UserEntity userEntity) {
		this.id = userEntity.getId();
		this.name = userEntity.getName();
		this.introduce = userEntity.getIntroduce();
		this.profileImage = userEntity.getProfileImage();
	}
}
