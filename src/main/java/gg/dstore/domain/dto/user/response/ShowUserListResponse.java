package gg.dstore.domain.dto.user.response;

import gg.dstore.domain.dto.user.dataIgnore.SelectUserDto;
import gg.dstore.domain.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ShowUserListResponse extends Response {
	List<SelectUserDto> userList;
	Boolean end;
}
