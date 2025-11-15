package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.docs.AuthControllerDocs;
import com.sprint.mission.discodeit.dto.user.request.UserLoginReq;
import com.sprint.mission.discodeit.dto.user.response.UserDetailInfoRes;
import com.sprint.mission.discodeit.facade.auth.AuthFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController implements AuthControllerDocs {

  //Fei
  private final AuthFacade authFacade;

  // 로그인
  @PostMapping("/login")
  public ResponseEntity<UserDetailInfoRes> login(@Valid @RequestBody UserLoginReq req) {
    return ResponseEntity.ok(authFacade.login(req));
  }
}
