package com.sprint.mission.discodeit.facade.auth;

import com.sprint.mission.discodeit.dto.auth.response.VerifyCodeRes;
import com.sprint.mission.discodeit.service.UserService;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailAuthFacade {

  private final UserService userService;
  private final JavaMailSender mailSender;
  private final Map<String, EmailAuth> emailAuthMap = new HashMap<>();

  // 인증번호 발송
  public void sendEmailCode(String email) {
    String code = UUID.randomUUID().toString();
    LocalDateTime expireAt = LocalDateTime.now().plusMinutes(5); // 5분 제한

    emailAuthMap.put(email, new EmailAuth(code, expireAt));

    // SimpleMail 보내기
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(email);
    message.setSubject("회원 인증 코드");
    message.setText("인증 코드: " + code + "\n유효시간: 5분");
    mailSender.send(message);
  }

  // 인증번호 검증
  public VerifyCodeRes verifyEmailCode(String email, String code) {
    EmailAuth auth = emailAuthMap.get(email);
    if (auth == null) {
      return new VerifyCodeRes(false, "인증 코드를 입력해야 합니다.");
    }
    if (!auth.code().equals(code)) {
      return new VerifyCodeRes(false, "인증 번호가 같지 않습니다.");
    }
    if (auth.expireAt().isAfter(LocalDateTime.now())) {
      return new VerifyCodeRes(false, "만료된 인증 코드입니다.");
    }
    return new VerifyCodeRes(true, "인증 완료");
  }
}
