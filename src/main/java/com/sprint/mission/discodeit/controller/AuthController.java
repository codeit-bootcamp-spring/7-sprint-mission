package com.sprint.mission.discodeit.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController { //  implements AuthDoc

//    private final InterfaceAuthService authService;

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
}
