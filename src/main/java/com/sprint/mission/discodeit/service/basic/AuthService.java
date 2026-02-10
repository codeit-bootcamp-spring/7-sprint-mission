package com.sprint.mission.discodeit.service.basic;

//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.exception.ErrorCode;
//import com.sprint.mission.discodeit.exception.UserException;
//import com.sprint.mission.discodeit.exception.UserNotFoundException;
//import com.sprint.mission.discodeit.mapper.dto.LoginRequest;
//import com.sprint.mission.discodeit.mapper.UserMapper;
//import com.sprint.mission.discodeit.mapper.dto.UserDto;
//import com.sprint.mission.discodeit.repository.jpa.UsersRepository;
//import com.sprint.mission.discodeit.service.InterfaceAuthService;
//import java.util.Map;
//import org.springframework.transaction.annotation.Transactional;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
import com.sprint.mission.discodeit.dto.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.mapper.dto.UserDto;
import com.sprint.mission.discodeit.repository.jpa.UsersRepository;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.InterfaceAuthService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
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
    @Transactional
    @Override
    public UserDto userRoleUpdateRequest(UserRoleUpdateRequest userRoleUpdateRequest) {

        User user = userRepository.findById(userRoleUpdateRequest.userId())
            .orElseThrow(() -> new UserNotFoundException(userRoleUpdateRequest.userId()));

        user.updateRole(userRoleUpdateRequest.newRole());

        // 현재 로그인 중이라면 세션 무효화
        expireUserSessions(user.getId());

        return userMapper.toDto(user);
    }

    @Transactional
    public void expireUserSessions(UUID userId) {

        sessionRegistry.getAllPrincipals().stream()
            .filter(DiscodeitUserDetails.class::isInstance)
            .map(DiscodeitUserDetails.class::cast)
            .filter(userDetails -> userDetails.getUser().id().equals(userId))
            .flatMap(userDetails -> sessionRegistry.getAllSessions(userDetails, false).stream())
            .forEach(SessionInformation::expireNow);
    }
}
