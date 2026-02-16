package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.dto.request.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.response.user.UserDto;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface UserService {

    UserDto createUser(UserCreateRequestDto userCreateRequestDtoDto, MultipartFile profile)  ;


    UserDto createAdmin(UserCreateRequestDto userCreateRequestDto);

    UserDto readUser(UUID userId);
    List<UserDto> readAllUser();
    void deleteUser(UUID userId);
    void resetUserRepository();
    List<UserDto> findAllUsers();
    UserDto patchUser(UUID userId, UserUpdateRequest dto, MultipartFile profile) ;

}
