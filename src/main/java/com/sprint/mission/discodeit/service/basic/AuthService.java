package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.dto.AuthServiceDto;
import com.sprint.mission.discodeit.dto.Res_UserLogin;
import com.sprint.mission.discodeit.repository.InterfaceUserRepository;
import com.sprint.mission.discodeit.service.InterfaceAuthService;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements InterfaceAuthService {
    private final InterfaceUserRepository userRepository;

    @Override
    public Res_UserLogin login(AuthServiceDto authServiceDto) {
      User user = userRepository.findByName(authServiceDto.username())
          .orElseThrow(
              () -> new NoSuchElementException("🚨사용자를 [" + authServiceDto.username() + "] 찾을 수 없음"));

      boolean equals = user.getPassword().equals(authServiceDto.password());
      if (!equals) {
        throw new IllegalArgumentException("🚨비밀번호["+ authServiceDto.password() + "]가 일치하지 않음");
      }

      return userRepository.isLogin(authServiceDto);
    }
}
