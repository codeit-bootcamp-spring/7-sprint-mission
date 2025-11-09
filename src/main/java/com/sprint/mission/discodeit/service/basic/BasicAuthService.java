package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.dto.userDto.UserResponseDto;
import com.sprint.mission.discodeit.exception.AuthenticationException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.entity.dto.loginDto.LoginRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;

    @Override
    public UserResponseDto login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByEmail(loginRequestDto.email())
                .orElseThrow(() -> new AuthenticationException("아이디 또는 비밀번호가 틀립니다."));
        if (!user.getPassword().equals(loginRequestDto.password())) {
            throw new AuthenticationException("아이디 또는 비밀번호가 틀립니다.");
        }
        return UserResponseDto.from(user, true);
    }
}
