package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.LoginUserDto;
import com.sprint.mission.discodeit.dto.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
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
    public UserResponseDto login(LoginUserDto request) {
        User user = userRepository.findByUserId(request.getUserId())
                .filter(u -> u.getPassword().equals(request.getPassword()))
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호를 잘못 입력하셨습니다."));

        userStatusRepository.updateLoginTime(user.getId());

        return UserResponseDto.from(user, true);
    }
}
