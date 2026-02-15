package com.sprint.mission.discodeit.api;

import com.sprint.mission.discodeit.dto.auth.JwtDto;
import com.sprint.mission.discodeit.dto.auth.RoleUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;

import java.util.Map;

@Tag(name = "Auth API", description = "인증 관련 API")
public interface AuthApi {

    ResponseEntity<Void> getCsrfToken(CsrfToken csrfToken);

    ResponseEntity<JwtDto> refreshAccessToken(String refreshToken, HttpServletResponse response);

    ResponseEntity<Map<String, Object>> getSessionInfo(HttpSession session);

    ResponseEntity<UserResponseDto> updateUserRole(RoleUpdateRequest request);
}
