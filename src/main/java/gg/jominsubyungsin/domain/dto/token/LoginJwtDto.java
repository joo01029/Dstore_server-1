package gg.jominsubyungsin.domain.dto.token;

import gg.jominsubyungsin.domain.response.Response;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;

@Getter
@Setter
public class LoginJwtDto {
	private String accessToken;
	private String accessExpiredTime;
	private String refreshToken;
	private String refreshExpiredTime;
}
