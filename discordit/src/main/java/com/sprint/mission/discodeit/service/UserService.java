package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.user.response.UserResponseV2;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponseV2 get(UUID uuid);
    List<UserResponseV2> getAllUsers();
    List<UserResponseV2> getOnlineUsers();


    UserResponseV2 signIn(UserCreateRequest dto);
    UserResponseV2 update(UUID id, UserUpdateRequest dto, MultipartFile profile);

    void delete(UUID id);
}
