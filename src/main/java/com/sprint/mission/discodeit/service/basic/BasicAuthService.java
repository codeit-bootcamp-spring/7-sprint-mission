package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.auth.request.LoginUserDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
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

    @Override
    public UserResponseDto login(LoginUserDto request) {
        User user = userRepository.findByLoginId(request.getLoginId())
                .filter(u -> u.getPassword().equals(request.getPassword()))
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호를 잘못 입력하셨습니다."));


        // 유저 최근 접속 시간 변경
        UserStatus status = userStatusRepository.findById(user.getId())
                        .orElseThrow(() -> new IllegalArgumentException("userstatus가 존재하지 않습니다."));
        status.setUpdatedAt();
        userStatusRepository.update(status);

        return UserResponseDto.from(user, true);
    }
}
