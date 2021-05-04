package gg.jominsubyungsin.domain.response.comment;

import gg.jominsubyungsin.domain.dto.query.SelectCommentDto;
import gg.jominsubyungsin.domain.entity.CommentEntity;
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
