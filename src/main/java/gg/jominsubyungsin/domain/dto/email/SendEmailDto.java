package gg.jominsubyungsin.domain.dto.email;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

@Getter
@Setter
public class SendEmailDto {
	@Email
	private String email;
}
