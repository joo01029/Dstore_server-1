package gg.jominsubyungsin.domain.dto.comment;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class GetCommentDto {
	@NotBlank
	private Long projectId;
	@NotBlank
	private String comment;
}
