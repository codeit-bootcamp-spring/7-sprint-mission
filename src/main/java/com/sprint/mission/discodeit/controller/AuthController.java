package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.docs.AuthControllerDocs;
import com.sprint.mission.discodeit.dto.request.LoginRequestDto;
import com.sprint.mission.discodeit.dto.response.LoginResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController implements AuthControllerDocs {

  private final AuthService authService;

  //사용자는 로그인할 수 있다.
  @PostMapping(value = "/login")
  public ResponseEntity<LoginResponseDto> login(
      @Valid @RequestBody LoginRequestDto request) {
    LoginResponseDto login = authService.login(request);
    return ResponseEntity.status(HttpStatus.OK).body(login);
  }

  @GetMapping("csrf-token")
  public ResponseEntity<Void> getCsrfToken(CsrfToken csrfToken) {
    String tokenValue = csrfToken.getToken();
    log.debug("CSRF Token: {}", tokenValue);
    return ResponseEntity.ok().build();
  }


}
