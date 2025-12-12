package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.dto.response.LoginResponseDto;
import com.sprint.mission.discodeit.dto.request.LoginRequestDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.LoginNotMatchException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;


  @Override
  @Transactional(readOnly = true)
  public LoginResponseDto login(LoginRequestDto request) {
    String username = request.username();
    String password = request.password();

    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new LoginNotMatchException());

    if (!user.getPassword().equals(password)) {
      throw new LoginNotMatchException();
    }
    return LoginResponseDto.from(user);
  }
}
