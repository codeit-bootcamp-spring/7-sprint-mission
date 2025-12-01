package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userDto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.userDto.UserDto;
import com.sprint.mission.discodeit.dto.userDto.UserUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface UserService {

    // 생성
    UserDto createUser(UserCreateRequest requestDto, MultipartFile profileImage);

    // 조회
    UserDto findUserById(UUID userId);

    List<UserDto> findAllUsers();

    // 수정
    UserDto updateUserInfo(UUID userId, UserUpdateRequest updateDto, MultipartFile profileImage);

    // 삭제
    void deleteUser(UUID userId); // 삭제 메서드 추가 (성공 여부 반환)
}