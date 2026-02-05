package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.docs.AuthControllerDocs;
import com.sprint.mission.discodeit.dto.response.UserResponseDto;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController implements AuthControllerDocs {

  @GetMapping("csrf-token")
  public ResponseEntity<Void> getCsrfToken(CsrfToken csrfToken) {
    String tokenValue = csrfToken.getToken();
    log.debug("CSRF Token: {}", tokenValue);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/me")
  public ResponseEntity<UserResponseDto> getUser(
      @AuthenticationPrincipal DiscodeitUserDetails userDetails) {

    log.info("현재 사용자 정보 조회: {}", userDetails.getUsername());

    UserResponseDto userDto = userDetails.getUserDto();
    return ResponseEntity.ok(userDto);
  }
}
