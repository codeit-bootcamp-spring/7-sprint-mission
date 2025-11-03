package com.sprint.mission.discodeit.application.facade;

import com.sprint.mission.discodeit.application.BasicUserService;
import com.sprint.mission.discodeit.application.UserStatusService;
import com.sprint.mission.discodeit.application.dto.request.UserCreateRequestDto;
import com.sprint.mission.discodeit.application.dto.request.UserRequestDto;
import com.sprint.mission.discodeit.application.dto.request.UserUpdateDto;
import com.sprint.mission.discodeit.application.dto.response.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserFacade {

    private final BasicUserService basicUserService;
    private final UserStatusService userStatusService;

    //BasicUserService
    public UserResponseDto createUser(UserCreateRequestDto requestDto) throws IOException {return basicUserService.createUser(requestDto);}
    public UserResponseDto updateUserInfo(UserUpdateDto updateDto) throws IOException {return basicUserService.updateUserInfo(updateDto);}
    public void delete(UserRequestDto requestDto) {basicUserService.delete(requestDto);}
    public UserResponseDto getUser(UserRequestDto userRequestDto) {return basicUserService.getUser(userRequestDto);}

    //UserStatusService
    public boolean checkUserOnline(UUID userId){return userStatusService.checkUserOnline(userId);}
}
