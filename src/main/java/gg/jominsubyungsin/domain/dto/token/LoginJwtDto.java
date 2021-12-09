package gg.jominsubyungsin.domain.dto.token;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginJwtDto {
	private String accessToken;
	private Long accessExpiredTime;
	private String refreshToken;
	private Long refreshExpiredTime;
}
