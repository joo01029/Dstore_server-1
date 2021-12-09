package gg.jominsubyungsin.domain.dto.user.response;

import gg.jominsubyungsin.domain.dto.project.dataIgnore.SelectProjectDto;
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
	private Long follower;
	private Long following;
	private Boolean follow;
	private List<SelectProjectDto> projects;
	private Boolean yourProfile;

	public UserDetailResponseDto(UserEntity userEntity, Boolean yourProfile, List<SelectProjectDto> projects,Long follower, Long following, Boolean follow) {
		this.id = userEntity.getId();
		this.name = userEntity.getName();
		this.introduce = userEntity.getIntroduce();
		this.profileImage = userEntity.getProfileImage();
		this.yourProfile = yourProfile;
		this.follower = follower;
		this.following = following;
		this.projects = projects;
	}
}
