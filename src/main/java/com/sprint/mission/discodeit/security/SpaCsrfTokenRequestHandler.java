package com.sprint.mission.discodeit.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

import java.util.function.Supplier;

public class SpaCsrfTokenRequestHandler extends CsrfTokenRequestAttributeHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, Supplier<CsrfToken> deferredCsrfToken) {
        super.handle(request, response, deferredCsrfToken);
        deferredCsrfToken.get();
    }
}
