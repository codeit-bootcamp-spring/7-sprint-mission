package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.response.ApiResponse;
import com.sprint.mission.discodeit.dto.user.request.UserLoginReq;
import com.sprint.mission.discodeit.facade.auth.AuthFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auths")
@RequiredArgsConstructor
public class AuthController {
    private final AuthFacade authFacade;

    // 로그인
    @RequestMapping(value ="/login", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse<Void>> login(@Valid @RequestBody UserLoginReq req){
        log.info("로그인 시도: {}", req.nickname());
        authFacade.login(req);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
