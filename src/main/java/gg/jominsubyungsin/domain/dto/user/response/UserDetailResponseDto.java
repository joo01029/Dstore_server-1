package gg.jominsubyungsin.domain.dto.user.response;

import gg.jominsubyungsin.domain.dto.query.SelectProjectDto;
import gg.jominsubyungsin.domain.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDetailResponseDto {
	private Long id;
	private String name;
	private String introduce;
	private String profileImage;
	private int follower;
	private int following;
	private List<SelectProjectDto> projects;
	private boolean yourProfile;

	public UserDetailResponseDto(UserEntity userEntity, boolean yourProfile, List<SelectProjectDto> projects) {
		this.id = userEntity.getId();
		this.name = userEntity.getName();
		this.introduce = userEntity.getIntroduce();
		this.profileImage = userEntity.getProfileImage();
		this.yourProfile = yourProfile;
		this.follower = 0;
		this.following = 0;
		this.projects = projects;
	}
}
