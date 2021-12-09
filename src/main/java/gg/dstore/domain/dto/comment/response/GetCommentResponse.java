package gg.dstore.domain.dto.comment.response;

import gg.dstore.domain.dto.comment.dataIgnore.SelectCommentDto;
import gg.dstore.domain.response.Response;
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
