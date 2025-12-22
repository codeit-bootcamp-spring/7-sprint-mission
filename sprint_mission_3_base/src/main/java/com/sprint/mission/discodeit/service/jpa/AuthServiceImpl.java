package com.sprint.mission.discodeit.service.jpa;

import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import com.sprint.mission.discodeit.exception.auth.InvalidCredentialsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;


@Primary
@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    @Override
    public UserDto login(LoginRequest loginRequest) {
        String emailOrUsername = loginRequest.username();
        String password = loginRequest.password();

        User user = userRepository.findByUsername(emailOrUsername)
                .orElseThrow(() -> new UserNotFoundException(emailOrUsername));

        if (!user.getPassword().equals(password)) {
            throw new InvalidCredentialsException(emailOrUsername);
        }

        return UserDto.from(user);
    }
}
