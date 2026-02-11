package com.sprint.mission.discodeit.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.config.JwtProperties;
import com.sprint.mission.discodeit.dto.request.authService.LoginRequestDto;
import com.sprint.mission.discodeit.dto.response.jwt.JwtDto;
import com.sprint.mission.discodeit.dto.response.jwt.JwtInformation;
import com.sprint.mission.discodeit.dto.response.user.UserDto;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;
    private final AuthService authService;
    private final JwtProperties jwtProperties;
    private final JwtRegistry jwtRegistry;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        LoginRequestDto loginRequest = new LoginRequestDto(userName, password);

        JwtDto jwtDto = authService.createAccessToken(loginRequest);
        String refreshToken = authService.createRefreshToken(loginRequest);

        UserDto userDto = jwtDto.userDto();
        String accessToken = jwtDto.accessToken();
        UUID userId = userDto.id();
        if(jwtRegistry.hasActiveJwtInformationByUserId(userId)){
            jwtRegistry.invalidateJwtInformationByUserId(userId);
        }
        JwtInformation jwtInformation = new JwtInformation(
                userDto,
                accessToken,
                refreshToken
        );
        jwtRegistry.registerJwtInformation(jwtInformation);


        Cookie refreshCookie = new Cookie("REFRESH_TOKEN", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(
                Math.toIntExact
                (jwtProperties.getRefreshTokenExpiration()));
        response.addCookie(refreshCookie);
        response.setContentType("application/json");
        objectMapper.writeValue(response.getWriter(), jwtDto);

    }



}
