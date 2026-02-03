package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.auth.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.security.DiscodeitUserDetailsService;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final SessionRegistry sessionRegistry;
    private final DiscodeitUserDetailsService userDetailsService;

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDto updateRole(UserRoleUpdateRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        user.updateRole(request.newRole());
        User saved = userRepository.save(user);

        DiscodeitUserDetails userDetails = (DiscodeitUserDetails) userDetailsService.loadUserByUsername(user.getUsername());

        List<SessionInformation> sessions = sessionRegistry.getAllSessions(userDetails, false);

        if (!sessions.isEmpty()) {
            sessions.forEach(SessionInformation::expireNow);
            log.info("사용자 {}의 모든 세션({}개) 무효화 완료", user.getUsername(), sessions.size());
        } else {
            log.info("사용자 {}에 활성 세션 없음", user.getUsername());
        }

        return userMapper.toResponseDto(saved);
    }
}
