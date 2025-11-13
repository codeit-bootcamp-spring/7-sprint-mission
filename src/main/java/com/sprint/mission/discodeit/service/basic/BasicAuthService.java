package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.AuthenticationException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.entity.dto.loginDto.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;

    @Override
    public User login(LoginRequest loginRequestDto) {
        User user = userRepository.findByUserName(loginRequestDto.username())
                .orElseThrow(() -> new AuthenticationException("아이디ㅁ 또는 비밀번호가 틀립니다."));
        if (!user.getPassword().equals(loginRequestDto.password())) {
            throw new AuthenticationException("아이디 또는 비밀번호ㅁ가 틀립니다.");
        }
        return user;
    }
}
