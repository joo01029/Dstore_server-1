package gg.jominsubyungsin.domain.dto.user.response;

import gg.jominsubyungsin.domain.dto.token.LoginJwtDto;
import gg.jominsubyungsin.domain.response.Response;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse extends Response {
	private LoginJwtDto tokens;
}
