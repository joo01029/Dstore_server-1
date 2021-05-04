package gg.jominsubyungsin.domain.dto.user.response;

import gg.jominsubyungsin.domain.dto.user.dataIgnore.SelectUserDto;
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
