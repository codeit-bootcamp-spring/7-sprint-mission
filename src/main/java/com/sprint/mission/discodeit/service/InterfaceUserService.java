package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.mapper.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.mapper.dto.UserDto;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface InterfaceUserService {
    UserDto create(UserCreateRequest userDt, Optional<MultipartFile> profileFile);
    UserDto find(UUID userID);      // 읽기
    List<UserDto> findAll();             // 모두 읽기
    UserDto update(UUID userId, UserUpdateRequest _userUpdateRequest, Optional<MultipartFile> profileFile); // 수정
    void delete(UUID userID);   // 삭제
}
