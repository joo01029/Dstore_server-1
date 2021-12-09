package gg.dstore.service.auth;

import gg.dstore.domain.dto.token.LoginJwtDto;
import gg.dstore.domain.dto.user.request.LoginDto;
import gg.dstore.domain.dto.user.request.UserDto;

public interface AuthService {
	void userCreate(UserDto userDto);

	LoginJwtDto login(LoginDto userDto);

	void sendMail(String email);

	LoginJwtDto makeTokens(String subject);

	void checkEmail(String email);

	Boolean authEmail(String code);
}
