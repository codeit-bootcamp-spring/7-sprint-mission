package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.docs.AuthControllerDocs;
import com.sprint.mission.discodeit.dto.auth.request.EmailCheckReq;
import com.sprint.mission.discodeit.dto.auth.request.UserLoginReq;
import com.sprint.mission.discodeit.dto.auth.request.VerifyCodeReq;
import com.sprint.mission.discodeit.dto.auth.response.AvailabilityRes;
import com.sprint.mission.discodeit.dto.auth.response.VerifyCodeRes;
import com.sprint.mission.discodeit.dto.user.response.UserDetailInfoRes;
import com.sprint.mission.discodeit.facade.auth.AuthFacade;
import com.sprint.mission.discodeit.service.basic.BasicAuthService;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController implements AuthControllerDocs {

  private final AuthFacade authFacade;
  private final BasicAuthService basicAuthService;

  // 로그인
  @PostMapping("/login")
  public ResponseEntity<UserDetailInfoRes> login(@Valid @RequestBody UserLoginReq req) {
    return ResponseEntity.ok(authFacade.login(req));
  }

  //이메일 인증 코드 발송
  @PostMapping("/email-code")
  public ResponseEntity<Void> sendEmailAuthCode(@Valid @RequestBody EmailCheckReq req) {
    String email = req.email();
    basicAuthService.sendEmailCode(email);
    return ResponseEntity.noContent().build();
  }

  //이메일 인증 결과 확인
  @PostMapping("/email-code/verify")
  public ResponseEntity<VerifyCodeRes> verifyEmailAuthCode(@Valid @RequestBody VerifyCodeReq req) {
    return ResponseEntity.ok(basicAuthService.verifyEmailCode(req.email(), req.code()));
  }
}
