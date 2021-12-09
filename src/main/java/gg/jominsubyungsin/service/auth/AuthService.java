package gg.jominsubyungsin.service.auth;

import gg.jominsubyungsin.domain.dto.token.LoginJwtDto;
import gg.jominsubyungsin.domain.dto.user.request.LoginDto;
import gg.jominsubyungsin.domain.dto.user.request.UserDto;

public interface AuthService {
	void userCreate(UserDto userDto);

	LoginJwtDto login(LoginDto userDto);

	void sendMail(String email);

	LoginJwtDto makeTokens(String subject);

	void checkEmail(String email);

	Boolean authEmail(String code);
}
