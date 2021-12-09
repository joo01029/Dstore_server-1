package gg.dstore.domain.dto.user.response;

import gg.dstore.domain.dto.user.dataIgnore.SelectUserDto;
import gg.dstore.domain.response.Response;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShowUserResponse extends Response {
	private SelectUserDto User;
}
