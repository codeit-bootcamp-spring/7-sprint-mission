package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.exception.UnAuthorizedException;
import com.sprint.mission.discodeit.mapper.dto.JwtDto;
import com.sprint.mission.discodeit.mapper.dto.UserDto;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.security.DiscodeitUserDetailsService;
import com.sprint.mission.discodeit.security.JwtTokenProvider;
import com.sprint.mission.discodeit.service.InterfaceAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController { //  implements AuthDoc

    private final InterfaceAuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final DiscodeitUserDetailsService userDetailsService;

//⭐️    CsrfToken 파라미터를 메서드 인자로 선언하면, HandlerMethodArgumentResolver를 통해 자동으로 주입됩니다. (공식문서)
//⭐️    GET 요청에는 CSRF 인증이 이루어지지 않기 때문에 토큰이 초기화되지 않습니다. 따라서 명시적으로 메소드에서 토큰을 호출합니다.
    @GetMapping("/csrf-token")
    public ResponseEntity<Void> getCsrfToken(CsrfToken csrfToken) {

        // 내용은 없어도 됩니다.
        // 이 API가 호출되는 과정에서 필터가 동작해 자동으로 쿠키를 구워줍니다.

        String tokenValue = csrfToken.getToken();
//        log.debug("CSRF 토큰 요청: {}", tokenValue);

        return ResponseEntity
            .status(HttpStatus.NON_AUTHORITATIVE_INFORMATION) // 203
            .build();
    }

    // 세션ID를 통해 사용자의 기본 정보(UserDto)를 가져올 수 있도록 정의한 API
    @GetMapping("/me")
    public ResponseEntity<UserDto> me(@AuthenticationPrincipal DiscodeitUserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserDto userDto = userDetails.getUser();

        log.debug("✅ me : {}", userDto.username());

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userDto);
    }


    @PostMapping
    public ResponseEntity<String> test() {
        return ResponseEntity.ok().body("테스트 성공!");
    }

//👍- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~                  : @RequestBody
//👍- http://localhost:8000/board?page=1&listSize=10  : @RequestParam
//👍- http://localhost:8000/board/1                   : @PathVariable
////🚨✅로그인 처리는 SecurityFilterChain에서 모두 처리
//    @PostMapping(value = "/login")
//    public ResponseEntity<UserDto> login(
//        @Valid @RequestBody LoginRequest loginRequest) {
//        //💎로그인
//        UserDto userDto
//            = authService.login(loginRequest);
//
//        return ResponseEntity
//            .status(HttpStatus.OK)
//            .body(userDto);
//    }

    @PutMapping("/role")
    public ResponseEntity<UserDto> roleUpdateRequest( @Valid @RequestBody UserRoleUpdateRequest userRoleUpdateRequest) {

        UserDto userDto =  authService.userRoleUpdateRequest(userRoleUpdateRequest);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userDto);
    }

//    permitAll 설정에 포함하세요.
//    이 API는 엑세스 토큰이 없거나 만료된 상태에서 호출하게 됩니다.
    @PostMapping("/refresh")
    public ResponseEntity<JwtDto> refreshAccessToken(
        @CookieValue(value = "REFRESH_TOKEN", required = false) String refreshToken
    ) {
        // 1️⃣ Refresh Token 존재 여부
        if (refreshToken == null) {
            throw new UnAuthorizedException("REFRESH_TOKEN이 없습니다.");
        }

        // 2️⃣ Refresh Token 유효성 검증
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new UnAuthorizedException("유효하지 않은 REFRESH_TOKEN입니다.");
        }

        // 3️⃣ Refresh Token에서 username 추출
        String username = jwtTokenProvider.getUsername(refreshToken);

        // 4️⃣ 사용자 조회
        DiscodeitUserDetails userDetails =
            userDetailsService.loadUserByUsername(username);

        // 5️⃣ 새 Access Token 발급
        String role = userDetails.getUser().role().name();
        String newAccessToken =
            jwtTokenProvider.createAccessToken(username, role);

        // 6️⃣ 응답 DTO 생성
        UserDto userDto = UserDto.builder()
            .id(userDetails.getUser().id())
            .username(username)
            .role(userDetails.getUser().role())
            .build();

        JwtDto jwtDto = JwtDto.builder()
            .userDto(userDto)
            .accessToken(newAccessToken)
            .build();

        return ResponseEntity.ok(jwtDto);
    }
}
