package gg.jominsubyungsin.domain.response.user;

import gg.jominsubyungsin.domain.dto.query.SelectUserDto;
import gg.jominsubyungsin.domain.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ShowUserListResponse extends Response {
	List<SelectUserDto> userList;
	Boolean end;
}
