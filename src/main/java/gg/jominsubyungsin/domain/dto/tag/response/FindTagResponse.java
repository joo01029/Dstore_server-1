package gg.jominsubyungsin.domain.dto.tag.response;

import gg.jominsubyungsin.domain.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FindTagResponse extends Response {
	List<String> tags;
}
