package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.doc.AuthDocs;
import com.sprint.mission.discodeit.dto.login.request.LoginRequestDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController implements AuthDocs {

  private final AuthService authService;

  @RequestMapping(value = "login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserResponseDto> login(
      @Valid @RequestBody LoginRequestDto loginRequestDto) {
    UserResponseDto userResponseDto = authService.login(loginRequestDto);
    return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
  }
}
