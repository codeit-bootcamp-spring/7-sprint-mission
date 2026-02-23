package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.auth.InvalidTokenException;
import com.sprint.mission.discodeit.jwt.GeneratedToken;
import com.sprint.mission.discodeit.jwt.JwtInformation;
import com.sprint.mission.discodeit.jwt.JwtRegistry;
import com.sprint.mission.discodeit.jwt.JwtTokenProvider;
import com.sprint.mission.discodeit.jwt.RefreshDto;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.dto.response.UserDto;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

//    private final UserSessionRegistry userSessionRegistry;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtRegistry jwtRegistry;
    private final UserRepository userRepository;

//    public void invalidateUserSession(UUID userId) {
//        userSessionRegistry.expireUserSessions(userId);
//    }
//
//    public boolean isUserLoggedIn(UUID userId) {
//        return userSessionRegistry.isUserLoggedIn(userId);
//    }

    public RefreshDto refresh(String refreshToken) {
        if (!jwtRegistry.hasActiveJwtInformationByRefreshToken(refreshToken)) {
            throw new InvalidTokenException(ErrorCode.INVALID_TOKEN, new HashMap<>());
        }

        Claims claims = jwtTokenProvider.validateToken(refreshToken);
        String tokenType = claims.get("token_type", String.class);
        if (!"refresh".equals(tokenType)) {
            throw new InvalidTokenException(ErrorCode.INVALID_TOKEN, new HashMap<>());
        }

        UUID userId = UUID.fromString(claims.get("id", String.class));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidTokenException(ErrorCode.INVALID_TOKEN, new HashMap<>()));

        UserDto userDto = UserDto.from(user);

        GeneratedToken generatedAccessToken = jwtTokenProvider.generateAccessToken(userDto);
        GeneratedToken generatedRefreshToken = jwtTokenProvider.generateRefreshToken(userDto);

        JwtInformation newJwtInformation = new JwtInformation(
                userDto,
                generatedAccessToken.getToken(),
                generatedRefreshToken.getToken(),
                generatedAccessToken.getExpiration(),
                generatedRefreshToken.getExpiration()
        );

        jwtRegistry.rotateJwtInformation(refreshToken, newJwtInformation);
        return new RefreshDto(userDto, generatedAccessToken.getToken(), generatedRefreshToken.getToken());
    }
}
