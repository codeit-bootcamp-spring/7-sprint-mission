package com.sprint.mission.discodeit.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.auth.JwtDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.security.jwt.config.JwtProperties;
import com.sprint.mission.discodeit.service.RefreshTokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;
    private final JwtProperties jwtProperties;
    private final RefreshTokenService refreshTokenService;
    private final CacheManager cacheManager;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Optional.ofNullable( cacheManager.getCache("users"))
                .ifPresent(Cache::clear);
        DiscodeitUserDetails userDetails = (DiscodeitUserDetails) authentication.getPrincipal();

        UserResponseDto userResponseDto = userDetails.getUserResponseDto();


        String accessToken = jwtTokenProvider.generateAccessToken(userResponseDto.id(), userResponseDto.email(), userResponseDto.username(), userResponseDto.role());
        String refreshToken = jwtTokenProvider.generateRefreshToken(userResponseDto.id(), userResponseDto.email());


        refreshTokenService.saveRefreshToken(userResponseDto.id(),  refreshToken);


        Cookie refreshTokenCookie = new Cookie("REFRESH_TOKEN", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setSecure(false); // NOTE: 로컬로 할거니 false
        refreshTokenCookie.setMaxAge(jwtProperties.getRefreshTokenExpirationMs().intValue() / 1000);

        response.addCookie(refreshTokenCookie);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");

        objectMapper.writeValue(response.getWriter(), new JwtDto(userResponseDto, accessToken));
    }
}
