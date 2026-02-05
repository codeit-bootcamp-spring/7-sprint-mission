package com.sprint.mission.discodeit.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;

@Tag(name = "Auth API", description = "인증 관련 API")
public interface AuthApi {
    ResponseEntity<Void> getCsrfToken(CsrfToken csrfToken);
}
