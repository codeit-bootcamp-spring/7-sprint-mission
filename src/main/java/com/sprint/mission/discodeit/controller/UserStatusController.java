package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.userstatus.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.userstatus.UserStatusUpdateByUserIdRequestDto;
import com.sprint.mission.discodeit.dto.request.userstatus.UserStatusUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.userstatus.UserStatusResponseDto;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-status")
@Slf4j
public class UserStatusController {
    private final UserStatusService userStatusService;

    @RequestMapping(method = RequestMethod.POST)
    public UserStatusResponseDto create(
            @Valid @RequestBody UserStatusCreateRequestDto userStatusCreateRequestDto) {
        log.debug("Received request to create user status.");
        return userStatusService.create(userStatusCreateRequestDto);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public UserStatusResponseDto update(@Valid @RequestBody UserStatusUpdateRequestDto userStatusUpdateRequestDto,
                                        @PathVariable("id") UUID id) {
        log.debug("Received request to update user status.");
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
        log.debug("Received request to update user status by user id.");
        return userStatusService.updateByUserId(userId, userStatusUpdateByUserIdRequestDto);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public UserStatusResponseDto get(@PathVariable UUID id) {
        log.debug("Received request to get read status.");
        return userStatusService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<UserStatusResponseDto> getAll() {
        log.debug("Received request to get all user status.");
        return userStatusService.getAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable UUID id) {
        log.debug("Received request to delete user status.");
        userStatusService.delete(id);
    }
}
