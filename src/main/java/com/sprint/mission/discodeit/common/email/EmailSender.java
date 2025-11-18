package com.sprint.mission.discodeit.common.email;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

// 이메일 발송 공통
@Component
@RequiredArgsConstructor
public class EmailSender {

  private final JavaMailSender mailSender;

  public void sendEmail(String to, String subject, String content) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(to);
    message.setSubject(subject);
    message.setText(content);
    mailSender.send(message);
  }
}
