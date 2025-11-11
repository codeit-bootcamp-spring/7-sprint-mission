package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.*;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.Instant;                    // 꼭 추가
import java.util.NoSuchElementException;


@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public LoginResponse login(LoginRequest request) {
        var user = userRepository.findByUsernameAndPassword(request.username(), request.password())
                .orElseThrow(() -> new NoSuchElementException("Invalid credentials"));

        // 마지막 접속 갱신
        userStatusRepository.findByUserId(user.getId()).ifPresentOrElse(
                us -> {
                    us.update(Instant.now());
                    userStatusRepository.save(us);
                },
                () -> userStatusRepository.save(new UserStatus(user.getId(), java.time.Instant.now()))
        );

        // online 계산은 UserService.find/findAll에서 해주므로 여기선 간단 응답
        UserDto dto = new UserDto(
                user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getUsername(),
                user.getEmail(),
                null, // profileId가 아직 없을 가능성이 높음
                true  // 로그인 시점이므로 online = true
        );
        return new LoginResponse(dto);
    }


}
