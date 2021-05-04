package gg.jominsubyungsin.domain.dto.like.response;

import gg.jominsubyungsin.domain.dto.like.dataIgnore.SelectLikeDto;
import gg.jominsubyungsin.domain.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetLikeListResponse extends Response {
	private List<SelectLikeDto> likes;
	private Boolean end;
}
