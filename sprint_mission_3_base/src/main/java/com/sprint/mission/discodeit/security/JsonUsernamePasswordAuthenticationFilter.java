package com.sprint.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JsonUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;

    public JsonUsernamePasswordAuthenticationFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        String contentType = request.getContentType();
        String principal;
        String password;

        if (contentType != null && contentType.startsWith("application/json")) {
            try {
                Map<String, Object> body = objectMapper.readValue(request.getInputStream(), Map.class);
                String email = body.get("email") == null ? null : String.valueOf(body.get("email"));
                String username = body.get("username") == null ? null : String.valueOf(body.get("username"));
                principal = (email != null && !email.isBlank()) ? email : username;
                password = body.get("password") == null ? "" : String.valueOf(body.get("password"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            principal = request.getParameter("username");
            if (principal == null || principal.isBlank()) {
                principal = request.getParameter("email");
            }
            password = request.getParameter("password");
            if (password == null) {
                password = "";
            }
        }

        UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(principal, password);

        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
