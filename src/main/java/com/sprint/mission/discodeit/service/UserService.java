package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.dto.request.binaryContent.ProfileCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.binaryContent.ProfileUpdateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserPatchRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.dto.request.userStatus.UserStatusPatchRequestDto;
import com.sprint.mission.discodeit.dto.response.UserCreateResponseDto;
import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.dto.response.UserReadResponseDto;
import com.sprint.mission.discodeit.dto.response.UserUserStatusPatchResponseDto;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface UserService {

    //UserCreateResponseDto createUser(UserCreateRequestDto userCreateRequestDtoDto);
    UserCreateResponseDto createUser(UserCreateRequestDto userCreateRequestDtoDto, MultipartFile profile) throws IOException;
    UserReadResponseDto readUser(UUID userId);
    List<UserReadResponseDto> readAllUser();
    void deleteUser(UUID userId);

    <T> void updateUser(UserUpdateRequestDto<T> userUpdateRequestDto);
    <T>void updateUser(UserUpdateRequestDto<T> userUpdateRequestDto, ProfileUpdateRequestDto profileUpdateRequestDto);
    List<User> readUpdatedUser();
   // void readDeletedUser();
    void enterChannel(UUID userId, UUID channelId);
    void exitChannel(UUID userId,UUID channelId);
    void updateUserOnlineStatus(UUID userId);
    void resetUserRepository();

    List<UserDto> advanceFindAllUser();
    UserCreateResponseDto patchUser(UUID userId, UserUpdateRequest dto,MultipartFile profile) throws IOException;

}
