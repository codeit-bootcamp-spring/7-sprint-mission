package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.config.jwt.JwtProvider;
import com.sprint.mission.discodeit.common.exceptions.auth.TokenNotValidException;
import com.sprint.mission.discodeit.common.exceptions.user.UserNotFoundException;
import com.sprint.mission.discodeit.dto.entity.auth.response.JwtDto;
import com.sprint.mission.discodeit.dto.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class BasicAuthService implements AuthService {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public JwtDto refresh(String refreshToken) {
        if (!jwtProvider.validateToken(refreshToken)) {
            throw new TokenNotValidException(refreshToken);
        }
        UUID userId = jwtProvider.getUserId(refreshToken);
        String newToken = jwtProvider.createRefreshToken(
                userId,
                jwtProvider.getUsername(refreshToken),
                jwtProvider.parseClaims(refreshToken).get("role", String.class)
        );

        return new JwtDto(userMapper.toDto(userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId))),
                newToken);
    }
}
