package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BasicAuthService {
    private UserRepository userRepository;

    public boolean login(String nickname, String password) {
        User user = userRepository.findByNickname(nickname);
        if(user.getPassword().equals(password)){
            return true;
        }
        // Todo : Userstatus의 상태를 online 으로 변경한다.

        return false;
    }
    public boolean logout(String nickname) {
        // Todo : Userstatus의 상태를 offline 으로 변경한다.
        return true;
    }
}
