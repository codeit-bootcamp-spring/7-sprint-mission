package com.sprint.mission.discodeit.service.login;

import com.sprint.mission.discodeit.dto.request.authService.LoginRequestDto;
import com.sprint.mission.discodeit.dto.response.LoginResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;
    @Override
    public LoginResponseDto checkLoginUser(LoginRequestDto loginRequestDto) {
        String userName = loginRequestDto.getUserName();
        String password = loginRequestDto.getPassword();
        List<User> userList = userRepository.getAllUser();
        Optional<User> optionalUser = userList.stream().filter(x -> x.getUserName().equals(userName) && x.getPassword().equals(password)).findFirst();
        User existUser = optionalUser.orElseThrow(IllegalArgumentException::new);
        return LoginResponseDto.of(existUser);
    }
}
