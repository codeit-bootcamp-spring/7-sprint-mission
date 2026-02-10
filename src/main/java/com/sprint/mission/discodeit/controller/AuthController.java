package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.security.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.dto.request.RoleUpdateRequest;
import com.sprint.mission.discodeit.service.dto.response.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    //커스텀 CsrfTokenRequestHandler가 이미 .getToken()을 하고 있기 때문에 아무 내용이 없어도 쿠키는 구워짐
    @GetMapping("/csrf-token")
    public ResponseEntity<Void> getCsrfToken(CsrfToken csrfToken) {
        String tokenValue = csrfToken.getToken();
        return ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION).build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(
            @AuthenticationPrincipal DiscodeitUserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(userDetails.getUserDto());
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/role")
    public ResponseEntity<UserDto> updateRole(
            @RequestBody RoleUpdateRequest roleUpdateRequest){

        UserDto userDto = userService.updateRole(roleUpdateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }
}
