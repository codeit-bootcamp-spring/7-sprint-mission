package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.login.request.LoginRequestDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.global.exception.CustomException;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
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

    @Transactional(readOnly = true)
    @Override
    public UserResponseDto login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByUsername(loginRequestDto.username())
                .orElseThrow(() -> new CustomException(ErrorCode.LOGIN_USER_NOT_FOUND));

        if (!loginRequestDto.password().equals(user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        userStatus.update(Instant.now());
        userStatusRepository.save(userStatus);

        return userMapper.toResponseDto(user);
    }
}
