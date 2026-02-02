package com.sprint.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.ErrorResponse;
import com.sprint.mission.discodeit.exception.domain.auth.InSufficientAccessException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DiscodeitAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        String username = request.getUserPrincipal() != null
                ? request.getUserPrincipal().getName()
                : "anonymous";

        if(request.getHeader("Accept")!=null && request.getHeader("Accept").contains("application/json"))
        {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            ErrorResponse errorResponse = new ErrorResponse(
                    Instant.now(),
                    ErrorCode.ACCESS_DENIED_ERROR.name(),
                    ErrorCode.ACCESS_DENIED_ERROR.getMessage(),
                    new HashMap<>() {{
                        put("username", username);
                    }},
                    "SomeThing",
                    403
            );
           response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        }

    }
}
