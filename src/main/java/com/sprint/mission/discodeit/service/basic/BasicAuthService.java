package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.dto.response.LoginResponseDto;
import com.sprint.mission.discodeit.dto.request.LoginRequestDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;


    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        if (user.getPassword().equals(password)) {
            LoginResponseDto dto = new LoginResponseDto(
                    user.getId(),
                    user.getUsername(),
                    user.getNickName(),
                    user.getEmail(),
                    user.getProfileId()
            );
            return dto;
        } else {
            throw new IllegalArgumentException("정보가 일치하지 않습니다.");
        }
    }
}
