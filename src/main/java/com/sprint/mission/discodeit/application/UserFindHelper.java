package com.sprint.mission.discodeit.application;

import com.sprint.mission.discodeit.application.dto.UserDtoMapper;
import com.sprint.mission.discodeit.application.dto.request.UserRequestDto;
import com.sprint.mission.discodeit.application.dto.response.UserResponseDto;
import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.domain.repository.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;


public final class UserFindHelper {

    public static User findById(UserRepository userRepository, UUID userId) {
        return userRepository.findById(userId).orElseThrow(()->new NoSuchElementException("해당 유저가 존재하지 않습니다."));
    }

    public static User findByUsername(UserRepository userRepository, String username){
        return userRepository.findByUsername(username).orElseThrow(()->new NoSuchElementException("아이디가 틀렸습니다"));
    }


    public static List<User> findAll(UserRepository userRepository) {
        return userRepository.findAll();
    }
}
