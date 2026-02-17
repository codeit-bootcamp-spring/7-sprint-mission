package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.jwt.JwtTokenProvider;
import com.sprint.mission.discodeit.jwt.RefreshDto;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.dto.response.UserDto;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

//    private final UserSessionRegistry userSessionRegistry;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

//    public void invalidateUserSession(UUID userId) {
//        userSessionRegistry.expireUserSessions(userId);
//    }
//
//    public boolean isUserLoggedIn(UUID userId) {
//        return userSessionRegistry.isUserLoggedIn(userId);
//    }

    public RefreshDto refresh(String refreshToken) {
        // 토큰에서 사용자 정보 추출
        Claims claims = jwtTokenProvider.validateToken(refreshToken);
        UUID userId = UUID.fromString(claims.get("id", String.class));

        // 사용자 존재 여부 확인
        User user = userRepository.findById(userId)
                .orElseThrow(RuntimeException::new);

        UserDto userDto = UserDto.from(user);

        // [Token Rotation] 신규 토큰 세트 생성
        String newAccessToken = jwtTokenProvider.generateAccessToken(userDto).getToken();
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(userDto).getToken();


        return new RefreshDto(userDto, newAccessToken, newRefreshToken);
    }
}
