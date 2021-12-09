package gg.dstore.domain.dto.like.response;

import gg.dstore.domain.dto.user.dataIgnore.SelectUserDto;
import gg.dstore.domain.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetLikeListResponse extends Response {
	private List<SelectUserDto> users;
	private Boolean end;
}
