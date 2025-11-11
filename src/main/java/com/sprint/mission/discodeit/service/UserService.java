package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// 생성 , 읽기 , 모두읽기, 수정, 삭제
public interface UserService {
    User create(UserCreateRequestDto userCreateRequestDto, UUID profileId);
    default User create(UserCreateRequestDto userCreateRequestDto) {
        return create(userCreateRequestDto, null);
    }
    User get(UUID uuid);
    List<User> getAll();
    User update(UserUpdateRequestDto userUpdateRequestDto, UUID profileId);
    default User update(UserUpdateRequestDto userUpdateRequestDto) {
        return update(userUpdateRequestDto, null);
    }

    boolean delete(UUID userid);
    List<User> getUsersByName(String username);
    Optional<User> getUsersByEmail(String email);
    void login(UUID userId);
    void logout(UUID userId);
    boolean isOnline(UUID userId);
}
