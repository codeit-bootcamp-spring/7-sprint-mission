package com.sprint.mission.discodeit.service.basic;

import com.nimbusds.jose.JOSEException;
import com.sprint.mission.discodeit.dto.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.dto.dto_Neo.RoleUpdatedEvent;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.dto.dto_Neo.JwtInformation;
import com.sprint.mission.discodeit.dto.dto_Neo.UserDto;
import com.sprint.mission.discodeit.repository.jpa.UsersRepository;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.security.jwt.JwtRegistry;
import com.sprint.mission.discodeit.security.jwt.JwtTokenProvider;
import com.sprint.mission.discodeit.service.InterfaceAuthService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

//🚨✅로그인 처리는 SecurityFilterChain에서 모두 처리

@Slf4j
@Service
//@Transactional // 영속성 컨텍스트
@RequiredArgsConstructor
public class AuthService implements InterfaceAuthService {
    private final UsersRepository userRepository;
    private final UserMapper userMapper;
    private final SessionRegistry sessionRegistry;
    private final JwtTokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;
    private final JwtRegistry jwtRegistry;
    private final ApplicationEventPublisher eventPublisher;

//    @Transactional(readOnly = true)
//    @Override
//    public UserDto login(LoginRequest loginRequest) {
//        User user = userRepository
//          .findUserByUsername(loginRequest.username())
//            .orElseThrow(() -> new UserNotFoundException(loginRequest.username()));
//
//        boolean equals = user.getPassword().equals(loginRequest.password());
//        if (!equals) {
//            throw new UserException(ErrorCode.USER_NOT_FOUND, Map.of("userPassword", loginRequest.password()));
//        }
//
//        log.info("✅login ok! - loginId = {}", user.getId());
//        return userMapper.toDto(user);
//    }

    @PreAuthorize("hasRole('ADMIN')")
//    @Transactional
    @Override
    @Transactional
    public UserDto userRoleUpdateRequest(UserRoleUpdateRequest userRoleUpdateRequest) {

        User user = userRepository.findById(userRoleUpdateRequest.userId())
            .orElseThrow(() -> new UserNotFoundException(userRoleUpdateRequest.userId()));

        String content = user.getRole().toString() + " -> " + userRoleUpdateRequest.newRole().toString();

        user.updateRole(userRoleUpdateRequest.newRole());
        eventPublisher.publishEvent(new RoleUpdatedEvent(user.getId(), content));

        jwtRegistry.invalidateJwtInformationByUserId(user.getId());

        return userMapper.toDto(user);
    }


    @Override
    public JwtInformation refreshToken(String refreshToken) {
        // Validate refresh token
        if (!tokenProvider.validateRefreshToken(refreshToken)) {
            throw new DiscodeitException(ErrorCode.INVALID_TOKEN, Map.of());
        }

        String username = tokenProvider.getUsernameFromToken(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!(userDetails instanceof DiscodeitUserDetails discodeitUserDetails)) {
            throw new DiscodeitException(ErrorCode.INVALID_USER_DETAILS, Map.of());
        }

        try {
            String newAccessToken = tokenProvider.generateAccessToken(discodeitUserDetails);
            String newRefreshToken = tokenProvider.generateRefreshToken(discodeitUserDetails);
            log.info("Access token refreshed for user: {}", username);
            return new JwtInformation(
                discodeitUserDetails.getUserDto(),
                newAccessToken,
                newRefreshToken
            );
        } catch (JOSEException e) {
            log.error("🚨 Failed to generate new tokens for user: {}", username, e);
            throw new DiscodeitException(ErrorCode.INTERNAL_SERVER_ERROR, Map.of("username", e));
        }
    }

    @Override
    public void clearExpiredJwtInfo() {
        jwtRegistry.clearExpiredJwtInformation();
    }
}
