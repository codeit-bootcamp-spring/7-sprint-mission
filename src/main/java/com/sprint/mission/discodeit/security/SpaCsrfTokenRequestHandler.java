package com.sprint.mission.discodeit.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.util.StringUtils;

import java.util.function.Supplier;

public class SpaCsrfTokenRequestHandler implements CsrfTokenRequestHandler {

    // 토큰 값을 암호화하지 않고 그대로 처리하는 객체
    private final CsrfTokenRequestHandler plain = new CsrfTokenRequestAttributeHandler();
    // 토큰 값을 암호화해서 변장시키고, 들어온 값의 변장을 풀어서 확인하는 객체
    private final CsrfTokenRequestHandler xor = new XorCsrfTokenRequestAttributeHandler();

    // 프론트엔드에서 /api/auth/csrf-token을 호출해서 토큰을 달라고 요청해도 spring은 당장 사용할거 아니면 토큰을 생성하지 않음.
    // 강제로 토큰 생성해서 쿠키에 담으라고 명령을 내림. -> response 헤더에 Set-Cookie라는 이름으로 토큰이 실려 나갑니다.
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, Supplier<CsrfToken> csrfToken) {
        // 기본적인 로직은 보안 담당자(xor)에게 처리를 맡김.
        this.xor.handle(request, response, csrfToken);

        // 토큰을 강제로 꺼냅니다.
        csrfToken.get();
    }

    // 프론트엔드가 토큰을 보내면 어떻게 해석할 지 결정하는 메서드
    @Override
    public String resolveCsrfTokenValue(HttpServletRequest request, CsrfToken csrfToken) {
        // 1. 요청 헤더에 실제 토큰 값이 들어있는 지 확인합니다.
        String headerValue = request.getHeader(csrfToken.getHeaderName());

        // 상황에 따라 다른 담당자를 부릅니다.
        // 헤더에 값이 있다? -> 프론트엔드가 쿠키에서 읽어서 직접 보낸 거구나! -> plain이 직접 처리.
        // 헤더에 값이 없다? -> 옛날 방식(form)이거나 다른 요청일 수 있구나. -> 기본 보안 방식대로 xor이 처리해라.
        return (StringUtils.hasText(headerValue) ? this.plain : this.xor).resolveCsrfTokenValue(request, csrfToken);
    }
}
