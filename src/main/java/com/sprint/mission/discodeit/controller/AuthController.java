package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.jwt.JwtDto;
import com.sprint.mission.discodeit.jwt.JwtProperties;
import com.sprint.mission.discodeit.jwt.RefreshDto;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.security.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.dto.request.RoleUpdateRequest;
import com.sprint.mission.discodeit.service.dto.response.UserDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;
    private final JwtProperties jwtProperties;

    //커스텀 CsrfTokenRequestHandler가 이미 .getToken()을 하고 있기 때문에 아무 내용이 없어도 쿠키는 구워짐
    @GetMapping("/csrf-token")
    public ResponseEntity<Void> getCsrfToken(CsrfToken csrfToken) {
        String tokenValue = csrfToken.getToken();
        return ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION).build();
    }

//    @GetMapping("/me")
//    public ResponseEntity<UserDto> getCurrentUser(
//            @AuthenticationPrincipal DiscodeitUserDetails userDetails) {
//
//        if (userDetails == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//
//        return ResponseEntity.status(HttpStatus.OK).body(userDetails.getUserDto());
//    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/role")
    public ResponseEntity<UserDto> updateRole(
            @RequestBody RoleUpdateRequest roleUpdateRequest){

        UserDto userDto = userService.updateRole(roleUpdateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtDto> getAccessToken(
            @CookieValue(name = "REFRESH_TOKEN") String refreshToken,
            HttpServletResponse response
    ){
        RefreshDto refresh = authService.refresh(refreshToken);
        UserDto userDto = refresh.getUserDto();
        String newAccessToken = refresh.getAccessToken();
        String newRefreshToken = refresh.getRefreshToken();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("REFRESH_TOKEN", newRefreshToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(jwtProperties.getRefreshTokenExpiration() / 1000)
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        JwtDto jwtDto = new JwtDto(userDto, newAccessToken);
        return ResponseEntity.status(HttpStatus.OK).body(jwtDto);
    }
}
