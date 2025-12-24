package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.exception.userstatus.InvalidUserStatusRequestException;
import com.sprint.mission.discodeit.dto.request.userstatus.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.userstatus.UserStatusUpdateByUserIdRequestDto;
import com.sprint.mission.discodeit.dto.request.userstatus.UserStatusUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.userstatus.UserStatusResponseDto;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    public UserStatusResponseDto create(
            @Valid @RequestBody UserStatusCreateRequestDto userStatusCreateRequestDto) {
        return userStatusService.create(userStatusCreateRequestDto);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public UserStatusResponseDto update(@Valid @RequestBody UserStatusUpdateRequestDto userStatusUpdateRequestDto,
                                        @PathVariable UUID id) {
        return userStatusService.update(userStatusUpdateRequestDto, id);
    }

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
        log.debug("Received request to get all user status.");
        return userStatusService.getAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        userStatusService.delete(id);
    }
}
