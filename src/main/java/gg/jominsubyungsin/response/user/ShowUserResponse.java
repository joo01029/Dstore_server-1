package gg.jominsubyungsin.response.user;

import gg.jominsubyungsin.domain.query.SelectUserDto;
import gg.jominsubyungsin.response.Response;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShowUserResponse extends Response {
  private SelectUserDto selectUserNoPrivacy;
}
