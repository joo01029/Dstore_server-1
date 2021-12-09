package gg.jominsubyungsin.domain.dto.comment.response;

import gg.jominsubyungsin.domain.dto.comment.dataIgnore.SelectCommentDto;
import gg.jominsubyungsin.domain.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class GetCommentResponse extends Response {
	private List<SelectCommentDto> comments = new ArrayList<>();
	private Boolean end;

}
