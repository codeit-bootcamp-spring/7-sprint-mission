package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.dto.request.binaryContent.ProfileCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.binaryContent.ProfileUpdateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.UserReadResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User createUser(UserCreateRequestDto userCreateRequestDtoDto);
    User createUser(UserCreateRequestDto userCreateRequestDtoDto, ProfileCreateRequestDto profileCreateRequestDtoDto);
    UserReadResponseDto readUser(UUID userId);
    List<UserReadResponseDto> readAllUser();
    void deleteUser(UUID userId);

    <T> void updateUser(UserUpdateRequestDto<T> userUpdateRequestDto);
    <T>void updateUser(UserUpdateRequestDto<T> userUpdateRequestDto, ProfileUpdateRequestDto profileUpdateRequestDto);
    List<User> readUpdatedUser();
   // void readDeletedUser();
    void enterChannel(UUID userId, UUID channelId);
    void exitChannel(UUID userId,UUID channelId);

}
