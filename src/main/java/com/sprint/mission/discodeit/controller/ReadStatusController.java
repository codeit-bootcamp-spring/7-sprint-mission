package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.readstatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.readstatus.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.readstatus.ReadStatusResponseDto;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/readStatuses")
@RequiredArgsConstructor
@Slf4j
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ReadStatusResponseDto create(
            @Valid @RequestBody ReadStatusCreateRequestDto readStatusCreateRequestDto) {
        return readStatusService.create(readStatusCreateRequestDto);
    }

    @RequestMapping(value = "/{readStatusId}", method = RequestMethod.PATCH)
    public ReadStatusResponseDto update(@PathVariable UUID readStatusId,
                                        @Valid @RequestBody ReadStatusUpdateRequestDto readStatusUpdateRequestDto) {
        return readStatusService.update(readStatusId ,readStatusUpdateRequestDto);
    }

    @RequestMapping(value = "/{readStatusId}", method = RequestMethod.GET)
    public ReadStatusResponseDto get(@PathVariable UUID readStatusId) {
        return readStatusService.get(readStatusId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ReadStatusResponseDto> getAllByUserId(@RequestParam("userId") UUID userId) {
        return readStatusService.getAllByUserId(userId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{readStatusId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable UUID readStatusId) {
        readStatusService.delete(readStatusId);
    }
}
