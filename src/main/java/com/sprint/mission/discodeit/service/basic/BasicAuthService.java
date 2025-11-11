package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.auth.request.LoginRequestDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.global.exception.custom.CustomException;
import com.sprint.mission.discodeit.global.exception.custom.ErrorCode;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public User login(LoginRequestDto request) {
        // 아이디 또는 비밀번호를 입력하지 않은 경우 예외 발생
        if (request.getUsername() == null || request.getUsername().isBlank() ||
                request.getPassword() == null || request.getPassword().isBlank()) {
            throw new CustomException(ErrorCode.INVALID_LOGIN_REQUEST);
        }

        // 아이디 또는 비밀번호 중 하나라도 일치하지 않으면 예외 발생
        User user = userRepository.findByUsername(request.getUsername())
                .filter(u -> u.getPassword().equals(request.getPassword()))
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_CREDENTIALS));

        // 유저 최근 접속 시간 변경
        UserStatus status = userStatusRepository.findById(user.getId())
                        .orElseThrow(() -> new CustomException(ErrorCode.USER_STATUS_NOT_FOUND));
        status.setUpdatedAt();
        userStatusRepository.update(status);

        return user;
    }

    @Override
    public boolean checkLoginInfo(String loginId, String password) {
        return userRepository.findByUsername(loginId)
                .filter(u -> u.getPassword().equals(password))
                .isPresent();
    }
}
