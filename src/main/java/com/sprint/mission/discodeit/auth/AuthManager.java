package com.sprint.mission.discodeit.auth;

import com.sprint.mission.discodeit.user.application.UserRepository;
import com.sprint.mission.discodeit.user.domain.User;
import com.sprint.mission.discodeit.user.presentation.dto.request.UserLoginRequestDto;
import com.sprint.mission.discodeit.userstatus.application.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

//AuthService의 이름을 바꿈, 어느 도메인에도 속하지 않는 전역 서비스/관리 클래스라는 걸 알기 쉽게 표현하기 위해서!
@Service
@RequiredArgsConstructor
public class AuthManager {

    private final UserRepository userRepository;

    public User login(String loginId, String password){
        return userRepository.findByUsername(loginId)
                .filter(m->m.getPassword().equals(password))
                .orElse(null);
    }

}
