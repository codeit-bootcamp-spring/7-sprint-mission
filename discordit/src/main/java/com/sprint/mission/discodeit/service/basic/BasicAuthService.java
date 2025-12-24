package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.exceptions.user.UserNotFoundException;
import com.sprint.mission.discodeit.dto.entity.auth.request.UserLoginRequest;
import com.sprint.mission.discodeit.dto.entity.auth.response.UserLoginResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;

    @Override
    public UserLoginResponse login(UserLoginRequest dto) {
        User user = userRepository.findByUsername(dto.username())
                .orElseThrow(() -> new UserNotFoundException(dto.username()));
        if (!user.getPassword().equals(dto.password())) {
            throw new IllegalStateException("아이디와 비밀번호가 다릅니다.");
        }
        return UserLoginResponse.toDto(user);
    }
}
