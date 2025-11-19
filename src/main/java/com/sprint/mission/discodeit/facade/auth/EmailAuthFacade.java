package com.sprint.mission.discodeit.facade.auth;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.sprint.mission.discodeit.common.email.EmailSender;
import com.sprint.mission.discodeit.dto.auth.response.VerifyCodeRes;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.service.UserService;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailAuthFacade {

  private final UserService userService;
  private final EmailSender emailSender;
  Cache<String, String> emailAuthCache = Caffeine.newBuilder()
      .expireAfterWrite(5, TimeUnit.MINUTES)  //5분 뒤 자동 삭제
      .maximumSize(1000) // 필요에 따라 최대 저장 개수 제한
      .build();

  // 인증번호 발송
  public void sendEmailCode(String email) {
    // 가입 된 이메일인지 먼저 확인.
    if (!userService.isRegisteredEmail(email).available()) {
      throw new CustomException(ErrorCode.EMAIL_NOT_FOUND);
    }

    String code = UUID.randomUUID().toString();

    emailAuthCache.invalidate(email); //기존에 발송한 인증 메일이 있으면 없애기
    emailAuthCache.put(email, code);

    // SimpleMail 보내기
    emailSender.sendEmail(
        email,
        "[ch-at] 이메일 인증코드",
        "이메일 인증 신청을 하지 않으신 경우 이 메일을 무시해주세요.\n" +
            "인증 코드: " + code + "\n" +
            "유효시간: 5분"
    );
  }

  // 인증번호 검증
  public VerifyCodeRes verifyEmailCode(String email, String code) {
    String auth = emailAuthCache.getIfPresent(email);
    if (auth == null || !auth.equals(code)) {
      return new VerifyCodeRes(false, "인증 코드가 일치하지 않거나 만료되었습니다."); // 5분 지나면 자동 삭제
    }

    // 인증 성공 시 Cache에서 제거
    emailAuthCache.invalidate(email);
    return new VerifyCodeRes(true, "인증 완료");
  }
}
