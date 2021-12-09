package gg.jominsubyungsin.domain.dto.like.response;

import gg.jominsubyungsin.domain.dto.user.dataIgnore.SelectUserDto;
import gg.jominsubyungsin.domain.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetLikeListResponse extends Response {
	private List<SelectUserDto> users;
	private Boolean end;
}
