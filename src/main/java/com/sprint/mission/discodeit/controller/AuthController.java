package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.userDto.RoleUpdateRequest;
import com.sprint.mission.discodeit.dto.userDto.UserDto;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;

    @GetMapping("/csrf-token")
    public ResponseEntity<Void> csrfToken(CsrfToken token) {

        String tokenValue = token.getToken();
        log.debug("CSRF 토큰 요청: {}", tokenValue);
        return ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION).build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getMe(@AuthenticationPrincipal DiscodeitUserDetails userDetails) {
        return ResponseEntity.ok(userDetails.getUserDto());
    }

    @PutMapping("/role")
    public ResponseEntity<UserDto> ModifyRole(@RequestBody RoleUpdateRequest updateRequest) {
        return ResponseEntity.ok(userService.updateUserRole(updateRequest));
    }
}
