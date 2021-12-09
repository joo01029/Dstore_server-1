package gg.dstore.domain.dto.user.response;

import gg.dstore.domain.dto.token.LoginJwtDto;
import gg.dstore.domain.response.Response;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse extends Response {
	private LoginJwtDto tokens;
}
