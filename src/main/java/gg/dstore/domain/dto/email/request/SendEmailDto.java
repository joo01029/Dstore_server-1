package gg.dstore.domain.dto.email.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

@Getter
@Setter
public class SendEmailDto {
	@Email
	private String email;
}
