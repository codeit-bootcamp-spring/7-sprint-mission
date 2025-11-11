package com.sprint.mission.discodeit.user;

import com.sprint.mission.discodeit.user.dto.AuthUserDTO;
import com.sprint.mission.discodeit.user.dto.LoginRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public AuthUserDTO login(LoginRequestDTO request) {
        String username = request.username();
        String password = request.password();
        User user = userRepository.findByUsername(username).orElseThrow(
                ()->new NoSuchElementException("사용자를 찾을 수 없습니다."));

        if(passwordEncoder.matches(password, user.getPassword())){
            return AuthUserDTO.from(user);
        }else{
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }
}
