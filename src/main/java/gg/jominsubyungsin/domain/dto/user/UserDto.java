package gg.jominsubyungsin.domain.dto.user;

import lombok.Getter;
import lombok.Setter;

import gg.jominsubyungsin.domain.entity.UserEntity;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserDto {
	private Long id;
	@Email
	@NotBlank
	private String email;
	@NotBlank
	private String password;
	@NotBlank
	private String name;
	private String introduce;
	private MultipartFile profileImage;

	public UserEntity toEntity() {
		return UserEntity.builder()
				.id(id)
				.email(email)
				.password(password)
				.name(name)
				.build();
	}
}
