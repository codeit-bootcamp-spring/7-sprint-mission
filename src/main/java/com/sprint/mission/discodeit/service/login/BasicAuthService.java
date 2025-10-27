package com.sprint.mission.discodeit.service.login;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.dto.userDto.UserInfoDto;
import com.sprint.mission.discodeit.exception.AuthenticationException;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BasicAuthService implements AuthSerivce{

    private final UserRepository userRepository;

    @Override
    public UserInfoDto login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByEmail(loginRequestDto.email())
                .orElseThrow(() -> new AuthenticationException("아이디 또는 비밀번호가 틀립니다."));
        if (!user.getPassword().equals(loginRequestDto.password())) {
            throw new AuthenticationException("아이디 또는 비밀번호가 틀립니다.");
        }
        return UserInfoDto.from(user, true);
    }
}
