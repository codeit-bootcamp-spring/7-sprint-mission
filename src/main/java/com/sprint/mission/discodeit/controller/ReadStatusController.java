package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.readstatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.readstatus.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.readstatus.ReadStatusResponseDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/read-status")
@RequiredArgsConstructor
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    @RequestMapping(method = RequestMethod.POST)
    public ReadStatusResponseDto create(
            @Valid @RequestBody ReadStatusCreateRequestDto readStatusCreateRequestDto) {
        ReadStatus readStatus = readStatusService.create(readStatusCreateRequestDto);
        return ReadStatusResponseDto.from(readStatus);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ReadStatusResponseDto update(@PathVariable UUID id,
                                        @Valid @RequestBody ReadStatusUpdateRequestDto readStatusUpdateRequestDto) {
        if(readStatusUpdateRequestDto.id() == null
        || !id.equals(readStatusUpdateRequestDto.id())) {
            throw new IllegalArgumentException("Invalid id!");
        }
        ReadStatus readStatus = readStatusService.update(readStatusUpdateRequestDto);
        return ReadStatusResponseDto.from(readStatus);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ReadStatusResponseDto get(@PathVariable UUID id) {
        ReadStatus readStatus = readStatusService.get(id);
        return ReadStatusResponseDto.from(readStatus);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ReadStatusResponseDto> getAllByUserId(@RequestParam("userId") UUID userId) {
        List<ReadStatus> all = readStatusService.getAllByUserId(userId);
        return all.stream()
                .map(rs -> ReadStatusResponseDto.from(rs))
                .toList();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable UUID id) {
        readStatusService.delete(id);
    }
}
