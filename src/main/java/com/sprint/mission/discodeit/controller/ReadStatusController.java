package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.doc.ReadStatusDocs;
import com.sprint.mission.discodeit.dto.readStatus.request.CreateReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.request.UpdateReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.response.ReadStatusResponseDto;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/readStatuses")
@RequiredArgsConstructor
public class ReadStatusController implements ReadStatusDocs {

  private final ReadStatusService readStatusService;

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<ReadStatusResponseDto> createReadStatus(
      @Valid @RequestBody CreateReadStatusDto createReadStatusDto) {
    ReadStatusResponseDto readStatusResponseDto = readStatusService.createReadStatus(
        createReadStatusDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(readStatusResponseDto);
  }

  @RequestMapping(value = "/{readStatusId}", method = RequestMethod.PATCH)
  public ResponseEntity<ReadStatusResponseDto> updateReadStatus(@PathVariable UUID readStatusId,
      @RequestBody UpdateReadStatusDto updateReadStatusDto) {
    ReadStatusResponseDto readStatusResponseDto = readStatusService.updateReadStatus(readStatusId,
        updateReadStatusDto);
    return ResponseEntity.status(HttpStatus.OK).body(readStatusResponseDto);
  }

  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<ReadStatusResponseDto>> getAllReadStatus(@RequestParam UUID userId) {
    List<ReadStatusResponseDto> list = readStatusService.getAllReadStatusByUserId(userId);
    return ResponseEntity.status(HttpStatus.OK).body(list);

  }
}
