package gg.jominsubyungsin.service.email;

import gg.jominsubyungsin.domain.dto.email.EmailDto;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {
  public void sendMail(EmailDto emailDto);
}
