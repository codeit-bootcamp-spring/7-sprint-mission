package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.mapper.UserMapper;
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

    private final UserMapper userMapper;

    @Override
    public boolean checkLoginInfo(String loginId, String password) {
        return userRepository.findByUsername(loginId)
                .filter(u -> u.getPassword().equals(password))
                .isPresent();
    }
}
