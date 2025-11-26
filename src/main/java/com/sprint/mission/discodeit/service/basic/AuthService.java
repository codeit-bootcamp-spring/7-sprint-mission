package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.dto.AuthServiceDto;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.mapper.dto.UserDto;
import com.sprint.mission.discodeit.repository.jpa.UsersRepository;
import com.sprint.mission.discodeit.service.InterfaceAuthService;
import jakarta.transaction.Transactional;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional // 영속성 컨텍스트
@RequiredArgsConstructor
public class AuthService implements InterfaceAuthService {
    private final UsersRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto login(AuthServiceDto authServiceDto) {
        User user = userRepository
          .findUserByUsername(authServiceDto.username())
          .orElseThrow(() -> new NoSuchElementException("🚨사용자를 [" + authServiceDto.username() + "] 찾을 수 없음"));

        boolean equals = user.getPassword().equals(authServiceDto.password());
        if (!equals) {
            throw new IllegalArgumentException("🚨비밀번호["+ authServiceDto.password() + "]가 일치하지 않음");
        }

        return userMapper.toDto(user);
    }
}
