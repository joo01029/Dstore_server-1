package gg.dstore.lib;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;

import javax.mail.internet.MimeMessage;

@EnableAsync
@Component
@RequiredArgsConstructor
public class EmailSender {
	private final JavaMailSender javaMailSender;
	private final Log log;

	@Value("${spring.mail.username}")
	private String senderEmail;

	@Async
	public void sendMail(String email, String title, String content) {
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
			log.error("send email error");
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "이메일 에러");
		}
	}
}
