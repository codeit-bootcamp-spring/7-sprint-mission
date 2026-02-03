package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.auth.AuthLoginRequestDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.auth.AuthInvalidCredentialsException;
import com.sprint.mission.discodeit.exception.userStatus.UserStatusNotFoundByUserIdException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponseDto login(AuthLoginRequestDto request) {
        log.debug("로그인 시도 username={}", request.username());
        User findUser = userRepository.findByUsername(
                        request.username())
                .orElseThrow(AuthInvalidCredentialsException::new);
        if (!passwordEncoder.matches(request.password(), findUser.getPassword())) {
            throw new AuthInvalidCredentialsException();
        }
        log.debug("사용자 조회 성공 userId={}", findUser.getId());
        // TODO: userState를 null이 아닌 online 값으로 추가
        UserStatus userStatus = userStatusRepository.findByUserId(findUser.getId()).orElseThrow(() -> new UserStatusNotFoundByUserIdException(findUser.getId()));
        userStatus.markAsActive();
        UserStatus saved = userStatusRepository.save(userStatus);
        log.debug("사용자 상태 업데이트 userId={}", findUser.getId());

        log.info("로그인 성공 userId={} username={}", findUser.getId(), findUser.getUsername());
        return userMapper.toDto(findUser); // TODO: 추후 컨트롤러 생기면 넘겨주는 속성만 전달하는 LoginResult로 변경
    }
}
