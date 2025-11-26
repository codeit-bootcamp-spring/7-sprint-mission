package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.request.LoginRequest;
import com.sprint.mission.discodeit.dto.user.response.LoginResponse;
import com.sprint.mission.discodeit.dto.user.response.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.NoSuchElementException;


@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    public UserDto login(LoginRequest loginRequest) {
        //이메일매칭
        /*      UserStatus userStatus = userstatusRepository.findByUserId(user.getId())
                .orElseThrow(() -> new NoSuchElementException("맞는유저ID가 없어"));*/

        //로그인하면 상태는 최신화 아닐까 생각해서 넣어봤다
        //LoginResponse.from(user, userStatus.isOnline());
        ;
        return userRepository
                .findByUsernameAndPassword(loginRequest.username(), loginRequest.password())
                .map(userMapper::toDto)
                .orElseThrow(() -> new NoSuchElementException(
                        "이름과 패스워드가 맞지않아 \n" +
                                "이름 :" + loginRequest.username() + "\n" +
                                "패스워드 :" + loginRequest.password() + "\n"));


    }
}


