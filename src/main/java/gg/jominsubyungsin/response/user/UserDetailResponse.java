package gg.jominsubyungsin.response.user;

import gg.jominsubyungsin.domain.dto.user.response.UserDetailResponseDto;
import gg.jominsubyungsin.response.Response;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserDetailResponse extends Response {
  UserDetailResponseDto user;
}
