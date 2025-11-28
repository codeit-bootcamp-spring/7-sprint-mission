package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.dto.LoginRequest;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.mapper.dto.UserDto;
import com.sprint.mission.discodeit.repository.jpa.UsersRepository;
import com.sprint.mission.discodeit.service.InterfaceAuthService;
import org.springframework.transaction.annotation.Transactional;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional // 영속성 컨텍스트
@RequiredArgsConstructor
public class AuthService implements InterfaceAuthService {
    private final UsersRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto login(LoginRequest loginRequest) {
        log.info("🩷UserDto login");
        User user = userRepository
          .findUserByUsername(loginRequest.username())
          .orElseThrow(() -> new NoSuchElementException("🚨사용자를 [" + loginRequest.username() + "] 찾을 수 없음"));

        boolean equals = user.getPassword().equals(loginRequest.password());
        if (!equals) {
            throw new IllegalArgumentException("🚨비밀번호["+ loginRequest.password() + "]가 일치하지 않음");
        }

        return userMapper.toDto(user);
    }
}
