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
        return userStatusService.create(userStatusCreateRequestDto);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public UserStatusResponseDto update(@Valid @RequestBody UserStatusUpdateRequestDto userStatusUpdateRequestDto,
                                        @PathVariable("id") UUID id) {
        if(userStatusUpdateRequestDto.id() == null || !id.equals(userStatusUpdateRequestDto.id())) {
            throw new IllegalArgumentException("Invalid ID");
        }
        return userStatusService.update(userStatusUpdateRequestDto);
    }

    // 쿼리파람
    @RequestMapping(value = "/by-user-id/{userId}", method = RequestMethod.PATCH)
    public UserStatusResponseDto updateByUserId(
            @Valid @RequestBody UserStatusUpdateByUserIdRequestDto userStatusUpdateByUserIdRequestDto,
            @PathVariable UUID userId) {
        return userStatusService.updateByUserId(userId, userStatusUpdateByUserIdRequestDto);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public UserStatusResponseDto get(@PathVariable UUID id) {
        return userStatusService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<UserStatusResponseDto> getAll() {
        return userStatusService.getAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable UUID id) {
        userStatusService.delete(id);
    }
}
