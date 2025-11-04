package com.sprint.mission.discodeit.service.jcf.basic;

import com.sprint.mission.discodeit.dto.user.request.LoginRequest;
import com.sprint.mission.discodeit.dto.user.response.LoginResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.NoSuchElementException;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;

   // public AuthService(UserRepository userRepository) {this.userRepository = userRepository;}

    public User login(LoginRequest loginRequest) {
        //이메일매칭
        User user = userRepository.findAll().stream()
                .filter(u -> u.getUserName().equals(loginRequest.username()))
                .filter(u -> u.getPassword().equals(loginRequest.password()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        "이름과 패스워드가 맞지않아 \n" +
                                "이름 :" + loginRequest.username() + "\n" +
                                "패스워드 :" + loginRequest.password() + "\n"));

        LoginResponse.from(user);
        return user;


    }
    }


