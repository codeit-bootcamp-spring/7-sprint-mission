package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.doc.AuthDocs;
import com.sprint.mission.discodeit.dto.auth.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.global.config.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController implements AuthDocs {

    private final AuthService authService;

    @GetMapping("/csrf-token")
    public ResponseEntity<Void> csrfToken(CsrfToken csrfToken) {
        String tokenValue = csrfToken.getToken();
        log.debug("CSRF 토큰 요청: {}", tokenValue);

        return ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION).build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getUserSession(@AuthenticationPrincipal DiscodeitUserDetails details) {
        if (details == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 로그인하지 않은 경우, UnAuthorized return;
        }
        UserResponseDto userResponseDto = details.getUserResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }

    @PutMapping("/role")
    public ResponseEntity<UserResponseDto> updateUserRole(UserRoleUpdateRequest userRoleUpdateRequest) {
        UserResponseDto userResponseDto = authService.updateRoleForAdmin(userRoleUpdateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }

}
