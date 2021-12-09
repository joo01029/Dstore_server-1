package gg.dstore.domain.dto.user.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDto {
	private String email;
	private String password;
	private String changePassword;
	private String changeName;
}
