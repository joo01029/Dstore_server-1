package gg.dstore.domain.dto.email.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailAccessDto {
	private String email;
	private String authKey;
}
