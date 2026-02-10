package com.sprint.mission.discodeit.global.config.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.util.StringUtils;

import java.util.function.Supplier;

public class SpaCsrfTokenRequestHandler implements CsrfTokenRequestHandler {
    // 토큰 값을 암호화 하지 않고 그대로 처리하는 객체
    private final CsrfTokenRequestHandler plain = new CsrfTokenRequestAttributeHandler();
    // 토큰 값을 XOR 인코딩하여 처리하는 객체
    private final CsrfTokenRequestHandler xor = new XorCsrfTokenRequestAttributeHandler();


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, Supplier<CsrfToken> csrfToken) {
        this.xor.handle(request, response, csrfToken);
        csrfToken.get();
    }

    @Override
    public String resolveCsrfTokenValue(HttpServletRequest request, CsrfToken csrfToken) {
        String headerValue = request.getHeader(csrfToken.getHeaderName());

        // 상황에 따라 다른 담당자를 부릅니다.
        // 헤더에 값이 있다? -> 프론트엔드가 쿠키에서 읽어서 직접 보낸 거구나! -> plain이 직접 처리.
        // 헤더에 값이 없다? -> 옛날 방식(form)이거나 다른 요청일 수 있구나. -> 기본 보안 방식대로 xor이 처리해라.
        return (StringUtils.hasText(headerValue) ? this.plain : this.xor).resolveCsrfTokenValue(request, csrfToken);
    }
}
