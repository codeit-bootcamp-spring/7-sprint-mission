package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.request.LoginRequest;
import com.sprint.mission.discodeit.dto.user.response.LoginResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final UserStatusRepository userstatusRepository;


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
        UserStatus userStatus = userstatusRepository.findByUserId(user.getId())
                .orElseThrow(() -> new NoSuchElementException("맞는유저ID가 없어"));

        //로그인하면 상태는 최신화 아닐까 생각해서 넣어봤다
        userstatusRepository.save(userStatus.getUserId());
        LoginResponse.from(user,userStatus.isOnline());
        return user;


    }
    }


