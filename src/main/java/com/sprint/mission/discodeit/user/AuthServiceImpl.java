package com.sprint.mission.discodeit.user;

import com.sprint.mission.discodeit.channel.ChannelService;
import com.sprint.mission.discodeit.message.channel.ChannelMessageService;
import com.sprint.mission.discodeit.message.direct.DirectMessageService;
import com.sprint.mission.discodeit.participation.ParticipationService;
import com.sprint.mission.discodeit.user.dto.AuthUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public AuthUserDTO login(String username, String password) {
        User user = userRepository.findByUsername(username).orElseThrow(
                ()->new NoSuchElementException("사용자를 찾을 수 없습니다."));

        if(passwordEncoder.matches(password, user.getPassword())){
            return AuthUserDTO.from(user);
        }else{
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }
}
