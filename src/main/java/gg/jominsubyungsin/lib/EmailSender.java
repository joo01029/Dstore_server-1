package gg.jominsubyungsin.lib;

import gg.jominsubyungsin.domain.dto.email.EmailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;

import javax.mail.internet.MimeMessage;

@Component
public class EmailSender {
  @Autowired
  private JavaMailSender javaMailSender;

  @Value("${spring.mail.username}")
  private String senderEmail;

  public void sendMail(String  email, String title, String content) {
    try {
      MimeMessage msg = javaMailSender.createMimeMessage();
      MimeMessageHelper messageHelper = new MimeMessageHelper(msg, true, "UTF-8");
      messageHelper.setFrom(senderEmail);
      messageHelper.setTo(email);
      messageHelper.setSubject(title);
      // 이메일 본문 (인코딩을 해야 한글이 깨지지 않음)
      messageHelper.setText(content, true);

      javaMailSender.send(msg);
    } catch (Exception e) {
      e.printStackTrace();
      throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "이메일 에러");
    }
  }
}
