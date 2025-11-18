package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/readStatuses")
public class ReadStatusController {

  private final ReadStatusService readStatusService;

  /**
   * Message 읽음 상태 생성 API
   * POST /api/readStatuses
   * - 요청 본문으로 ReadStatusCreateRequest(JSON)를 받는다.
   * - 이미 존재하는 조합(UserId + ChannelId)일 경우 서비스 레이어에서 예외를 던진다.
   * 생성된 ReadStatus 엔티티 전체를 201(CREATED) 상태 코드와 함께 반환한다.
   */
  @RequestMapping(
      method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<ReadStatus> create(@RequestBody ReadStatusCreateRequest request) {
    ReadStatus createdReadStatus = readStatusService.create(request);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createdReadStatus);
  }

  /**
   * Message 읽음 상태 수정 API
   * PATCH /api/readStatuses/{readStatusId}
   * - path variable로 수정할 ReadStatus ID를 받고
   * - 요청 본문으로 ReadStatusUpdateRequest(JSON)를 받는다.
   * lastReadAt 값을 갱신하고, 수정된 ReadStatus 엔티티를 반환한다.
   */
  @RequestMapping(
      method = RequestMethod.PATCH,
      value = "/{readStatusId}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<ReadStatus> update(
      @PathVariable("readStatusId") UUID readStatusId,
      @RequestBody ReadStatusUpdateRequest request
  ) {
    ReadStatus updatedReadStatus = readStatusService.update(readStatusId, request);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(updatedReadStatus);
  }

  /**
   * 특정 User의 Message 읽음 상태 목록 조회 API
   * GET /api/readStatuses?userId={userId}
   * - query parameter로 User ID를 받고,
   * - 해당 사용자의 ReadStatus 리스트를 반환한다.
   */
  @RequestMapping(
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<List<ReadStatus>> findAllByUserId(
      @RequestParam("userId") UUID userId
  ) {
    List<ReadStatus> readStatuses = readStatusService.findAllByUserId(userId);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(readStatuses);
  }
}
