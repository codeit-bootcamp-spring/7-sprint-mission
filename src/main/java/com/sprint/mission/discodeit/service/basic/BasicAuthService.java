package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.auth.request.LoginRequestDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.auth.InvalidCredentialsException;
import com.sprint.mission.discodeit.global.exception.auth.InvalidLoginRequestException;
import com.sprint.mission.discodeit.global.exception.userstatus.UserStatusNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponseDto login(LoginRequestDto request) {
        // 아이디 또는 비밀번호를 입력하지 않은 경우 예외 발생
        if (request.username() == null || request.username().isBlank() ||
                request.password() == null || request.password().isBlank()) {
            throw new InvalidLoginRequestException(ErrorCode.INVALID_LOGIN_REQUEST);
        }

        // 아이디 또는 비밀번호 중 하나라도 일치하지 않으면 예외 발생
        User user = userRepository.findByUsername(request.username())
                .filter(u -> u.getPassword().equals(request.password()))
                .orElseThrow(() -> new InvalidCredentialsException(ErrorCode.INVALID_CREDENTIALS));

        // 유저 최근 접속 시간 변경
        UserStatus status = userStatusRepository.findByUserId(user.getId())
                        .orElseThrow(() -> new UserStatusNotFoundException(ErrorCode.USER_STATUS_NOT_FOUND));
        status.update(Instant.now());
        userStatusRepository.save(status);

        return userMapper.toResponseDto(user);
    }

    @Override
    public boolean checkLoginInfo(String loginId, String password) {
        return userRepository.findByUsername(loginId)
                .filter(u -> u.getPassword().equals(password))
                .isPresent();
    }
}
