package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.dto.binaryContentDto.UserProfileImageUpdateDto;
import com.sprint.mission.discodeit.entity.dto.userDto.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface UserService {

    // 생성
    User createUser(UserRequestDto requestDto, MultipartFile profileImage);

    // 조회
    UserResponseDto findUserById(UUID userId);

    List<UserResponseDto> findAllUsers();

    // 수정
    User updateUserInfo(UUID userId, UserUpdateDto updateDto, MultipartFile profileImage);

    // 삭제
    void deleteUser(UUID userId); // 삭제 메서드 추가 (성공 여부 반환)
}