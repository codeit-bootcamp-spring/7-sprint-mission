package com.sprint.mission.discodeit.application;

import com.sprint.mission.discodeit.domain.OnlineStatus;
import com.sprint.mission.discodeit.domain.repository.UserRepository;
import com.sprint.mission.discodeit.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public final class AuthService {

    private final UserRepository userRepository;

    public User login(String loginId, String password) {
        User user = UserFindHelper.findByUsername(userRepository, loginId);
        if (!user.getPassword().equals(password)){
            throw new IllegalArgumentException("비밀번호가 틀립니다");
        }
        user.markOnline();
        return user;
    }

    public void logout(UUID userId){
        User user = UserFindHelper.findById(userRepository, userId);
        user.markOffline();
    }
}
