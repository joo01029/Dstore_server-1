package gg.jominsubyungsin.domain.dto.email;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

@Getter @Setter
public class EmailDto {
  @Email
  private String senderName;
  private String senderMail;
  @Email
  private String receiveMail;
  private String subject;
  private String message;

  @Override
  public String toString() {
    return "EmailDTO [senderName=" + senderName + ", senderMail=" + senderMail + ", receiveMail=" + receiveMail
            + ", subject=" + subject + ", message=" + message + "]";
  }
}
