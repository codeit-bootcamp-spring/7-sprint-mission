package com.sprint.mission.discodeit.common.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.common.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.common.security.SessionOnlineChecker;
import com.sprint.mission.discodeit.dto.response.auth.AuthTokenResponseDto;
import com.sprint.mission.discodeit.dto.response.auth.JwtDto;
import com.sprint.mission.discodeit.dto.response.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtLoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;
    private final RefreshTokenCookieManager refreshTokenCookieManager;
    private final UserRepository userRepository;
    private final SessionOnlineChecker sessionOnlineChecker;
    private final UserMapper userMapper;
    private final JwtRegistry jwtRegistry;
    private final JwtOnlineChecker jwtOnlineChecker;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        DiscodeitUserDetails principal = (DiscodeitUserDetails) authentication.getPrincipal();

        UUID userId = principal.getUserDto().id();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String role = user.getRole().name();
        AuthTokenResponseDto pair = jwtTokenProvider.issueTokenPair(userId, role);

        refreshTokenCookieManager.set(response, pair.refreshToken());

        boolean online = jwtOnlineChecker.isOnline(userId);
        UserResponseDto userDto = userMapper.toDto(user, online);

        JwtInformation info = new JwtInformation(
                userDto,
                pair.accessToken(),
                jwtTokenProvider.getExpirationInstant(pair.accessToken()),
                pair.refreshToken(),
                jwtTokenProvider.getExpirationInstant(pair.refreshToken())
        );

        jwtRegistry.registerJwtInformation(info);

        JwtDto jwtDto = new JwtDto(userDto, pair.accessToken());

        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), jwtDto);
    }
}
