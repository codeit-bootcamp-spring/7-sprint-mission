package com.sprint.mission.discodeit.api;

import com.sprint.mission.discodeit.dto.auth.RoleUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;

import java.util.Map;

@Tag(name = "Auth API", description = "인증 관련 API")
public interface AuthApi {

    ResponseEntity<UserResponseDto> getMe(DiscodeitUserDetails principal);

    ResponseEntity<Void> getCsrfToken(CsrfToken csrfToken);

    ResponseEntity<Map<String, Object>> getSessionInfo(HttpSession session);

    ResponseEntity<UserResponseDto> updateUserRole(RoleUpdateRequest request);
}
