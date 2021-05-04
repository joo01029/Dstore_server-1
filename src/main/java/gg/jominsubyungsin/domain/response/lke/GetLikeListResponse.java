package gg.jominsubyungsin.domain.response.lke;

import gg.jominsubyungsin.domain.dto.query.SelectLikeDto;
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
