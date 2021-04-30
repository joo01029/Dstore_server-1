package gg.jominsubyungsin.domain.dto.email;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailAccessDto {
	private String email;
	private String authKey;
}
