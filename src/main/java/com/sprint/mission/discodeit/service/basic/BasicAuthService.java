package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.auth.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.enums.Role;
import com.sprint.mission.discodeit.event.RoleUpdatedEvent;
import com.sprint.mission.discodeit.global.config.security.jwt.JwtInformation;
import com.sprint.mission.discodeit.global.config.security.jwt.JwtProvider;
import com.sprint.mission.discodeit.global.config.security.jwt.JwtRegistry;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.discodietException.auth.JwtTokenException;
import com.sprint.mission.discodeit.global.exception.discodietException.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtProvider jwtProvider;
    private final JwtRegistry jwtRegistry;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDto updateRoleForAdmin(UserRoleUpdateRequest request) {
        return updateRole(request);
    }

    @Transactional
    public UserResponseDto updateRole(UserRoleUpdateRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> UserNotFoundException.byId(request.userId()));
        Role from = user.getRole();
        user.updateRole(request.newRole());
        jwtRegistry.invalidateJwtInformationByUserId(user.getId());
        eventPublisher.publishEvent(
                new RoleUpdatedEvent(user.getId(), from, request.newRole())
        );
        return userMapper.toResponseDto(user);
    }

    public JwtInformation reIssuerAccessToken(String refreshToken) {
        if (!jwtProvider.validateRefreshToken(refreshToken)
                || !jwtRegistry.hasActiveJwtInformationByRefreshToken(refreshToken)) {
            throw JwtTokenException.byInvalidToken(ErrorCode.INVALID_TOKEN, refreshToken);
        }

        String username = jwtProvider.extractSubject(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> UserNotFoundException.byUsername(username));

        UserResponseDto userResponseDto = userMapper.toResponseDto(user);

        String newAccessToken = jwtProvider.generateAccessToken(userResponseDto.username());
        String newRefreshToken = jwtProvider.generateRefreshToken(userResponseDto.username());


        JwtInformation jwtInformation = new JwtInformation(
                userResponseDto,
                newAccessToken,
                newRefreshToken
        );
        jwtRegistry.rotateJwtInformation(refreshToken, jwtInformation);

        return jwtInformation;
    }
}
