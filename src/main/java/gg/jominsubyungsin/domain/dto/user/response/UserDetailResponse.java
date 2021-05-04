package gg.jominsubyungsin.domain.dto.user.response;

import gg.jominsubyungsin.domain.dto.user.response.UserDetailResponseDto;
import gg.jominsubyungsin.domain.response.Response;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetailResponse extends Response {
	UserDetailResponseDto user;
	Boolean end;
}
