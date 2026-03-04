package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.auth.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.global.event.RoleUpdatedEvent;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.jwt.BusinessException;
import com.sprint.mission.discodeit.global.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.jwt.JwtDto;
import com.sprint.mission.discodeit.security.jwt.JwtInformation;
import com.sprint.mission.discodeit.security.jwt.JwtRegistry;
import com.sprint.mission.discodeit.security.jwt.JwtTokenProvider;
import com.sprint.mission.discodeit.service.AuthService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.util.Pair;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtRegistry jwtRegistry;

    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDto updateRole(UserRoleUpdateRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        Role oldRole = user.getRole();

        user.updateRole(request.newRole());
        User saved = userRepository.save(user);

        eventPublisher.publishEvent(new RoleUpdatedEvent(
                request.userId(),
                oldRole,
                request.newRole()
        ));

       jwtRegistry.invalidateJwtInformationByUserId(user.getId());

        log.info("사용자 {} 권한 변경 → 모든 JWT 무효화 완료", user.getUsername());

        return userMapper.toResponseDto(saved);
    }

    @Transactional
    public Pair<JwtDto, String> refreshAccessToken(String refreshToken) {
        if (refreshToken == null || !jwtTokenProvider.isRefreshTokenValid(refreshToken)) {
            throw new BusinessException(ErrorCode.EMPTY_OR_INVALID_TOKEN);
        }

        Claims claims = jwtTokenProvider.validateToken(refreshToken, false);
        String username = claims.getSubject();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        String newAccessToken = jwtTokenProvider.generateAccessToken(user);

        // Refresh Token Rotation
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(user);

        jwtRegistry.rotateJwtInformation(refreshToken, new JwtInformation(
                userMapper.toResponseDto(user),
                newAccessToken,
                newRefreshToken
        ));


        JwtDto jwtDto = new JwtDto(userMapper.toResponseDto(user), newAccessToken);

        return Pair.of(jwtDto, newRefreshToken);
    }
}
