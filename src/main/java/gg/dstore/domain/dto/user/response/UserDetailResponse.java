package gg.dstore.domain.dto.user.response;

import gg.dstore.domain.response.Response;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetailResponse extends Response {
	UserDetailResponseDto user;
	Boolean end;
}
