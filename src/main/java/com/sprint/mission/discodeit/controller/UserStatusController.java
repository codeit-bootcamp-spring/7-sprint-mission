package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.userstatus.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.userstatus.UserStatusUpdateByUserIdRequestDto;
import com.sprint.mission.discodeit.dto.request.userstatus.UserStatusUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.userstatus.UserStatusResponseDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-status")
public class UserStatusController {
    private final UserStatusService userStatusService;

    @RequestMapping(method = RequestMethod.POST)
    public UserStatusResponseDto create(@Valid @RequestBody UserStatusCreateRequestDto userStatusCreateRequestDto) {
        UserStatus userStatus = userStatusService.create(userStatusCreateRequestDto);
        return  UserStatusResponseDto.from(userStatus);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public UserStatusResponseDto update(@Valid @RequestBody UserStatusUpdateRequestDto userStatusUpdateRequestDto,
                                        @PathVariable("id") UUID id) {
        if(userStatusUpdateRequestDto.id() == null || !id.equals(userStatusUpdateRequestDto.id())) {
            throw new IllegalArgumentException("Invalid ID");
        }
        UserStatus userStatus = userStatusService.update(userStatusUpdateRequestDto);
        return  UserStatusResponseDto.from(userStatus);
    }

    @RequestMapping(value = "/by-user-id", method = RequestMethod.PUT)
    public UserStatusResponseDto updateByUserId(@Valid @RequestBody UserStatusUpdateByUserIdRequestDto userStatusUpdateByUserIdRequestDto) {
        UserStatus userStatus = userStatusService.updateByUserId(userStatusUpdateByUserIdRequestDto);
        return UserStatusResponseDto.from(userStatus);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public UserStatusResponseDto get(@PathVariable UUID id) {
        UserStatus userStatus = userStatusService.get(id);
        return  UserStatusResponseDto.from(userStatus);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<UserStatusResponseDto> getAll() {
        List<UserStatus> userStatusList = userStatusService.getAll();
        return userStatusList.stream()
                .map(userStatus -> UserStatusResponseDto.from(userStatus))
                .toList();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable UUID id) {
        userStatusService.delete(id);
    }
}
