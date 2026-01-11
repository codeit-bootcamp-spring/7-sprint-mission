package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.exceptions.user.UserNotFoundException;
import com.sprint.mission.discodeit.dto.entity.auth.request.UserLoginRequest;
import com.sprint.mission.discodeit.dto.entity.auth.response.UserLoginResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;

    private final Counter loginRequestCounter;
    private final Counter loginFailedCounter;

    public BasicAuthService(UserRepository userRepository,
                            MeterRegistry meterRegistry) {
        this.userRepository = userRepository;

        this.loginRequestCounter = Counter.builder("auth.requested")
                .description("로그인 요청 횟수")
                .tag("type", "auth")
                .register(meterRegistry);

        this.loginFailedCounter = Counter.builder("auth.failed")
                .description("존재하는 아이디에 대해 비밀번호를 틀려 로그인이 실패한 경우")
                .tag("type", "order")
                .register(meterRegistry);
    }

    @Override
    public UserLoginResponse login(UserLoginRequest dto) {
        log.info("로그인 요청 들어옴. - {}", dto.username());
        loginRequestCounter.increment();
        User user = userRepository.findByUsername(dto.username())
                .orElseThrow(() -> new UserNotFoundException(dto.username()));

        if (!user.getPassword().equals(dto.password())) {
            log.warn("로그인 실패 - 아이디 비밀번호 불일치");
            loginFailedCounter.increment();
            throw new IllegalStateException("아이디와 비밀번호가 다릅니다.");
        }
        log.info("로그인 성공 - {}", user.getId());
        return UserLoginResponse.toDto(user);
    }
}
