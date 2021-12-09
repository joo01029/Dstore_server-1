package gg.jominsubyungsin.domain.dto.user.response;

import gg.jominsubyungsin.domain.dto.user.dataIgnore.SelectUserDto;
import gg.jominsubyungsin.domain.response.Response;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShowUserResponse extends Response {
	private SelectUserDto User;
}
