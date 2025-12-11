package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.loginDto.LoginRequest;
import com.sprint.mission.discodeit.dto.userDto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.AuthenticationException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public UserDto login(LoginRequest loginRequestDto) {
        User user = userRepository.findByUsername(loginRequestDto.username())
                .orElseThrow(() -> new AuthenticationException("아이디 또는 비밀번호가 틀립니다."));
        if (!user.getPassword().equals(loginRequestDto.password())) {
            throw new AuthenticationException("아이디 또는 비밀번호가 틀립니다.");
        }
        return userMapper.toDto(user);
    }
}
