package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.docs.ReadStateControllerDocs;
import com.sprint.mission.discodeit.dto.readstatus.request.ReadStatusCreateReq;
import com.sprint.mission.discodeit.dto.readstatus.response.ReadStatusInfoRes;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.facade.readstatus.ReadStatusCreateFacade;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/read-statuses")
@RequiredArgsConstructor
public class ReadStatusController implements ReadStateControllerDocs {

  private final ReadStatusCreateFacade readStatusCreateFacade;
  private final ReadStatusService readStatusService;

  //메세지 수신 정보 생성
  @PostMapping
  public ResponseEntity<ReadStatusInfoRes> createReadStatus(@RequestBody ReadStatusCreateReq req) {
    ReadStatus readStatus = readStatusCreateFacade.create(req);
    URI location = ServletUriComponentsBuilder
        .fromCurrentRequestUri()
        .path("/{id}")
        .buildAndExpand(readStatus.getId())
        .toUri();

    return ResponseEntity.created(location).body(ReadStatusInfoRes.from(readStatus));
  }

  //메세지 수신 정보 업데이트
  @PatchMapping("/{readStatusId}")
  public ResponseEntity<ReadStatusInfoRes> updateReadStatus(@PathVariable UUID readStatusId) {
    ReadStatus readStatus = readStatusService.update(readStatusId);
    return ResponseEntity.ok(ReadStatusInfoRes.from(readStatus));
  }

  //메세지 수신 정보 조회
  @GetMapping("/{readStatusId}")
  public ResponseEntity<ReadStatusInfoRes> getReadStatus(@PathVariable UUID readStatusId) {
    return ResponseEntity.ok(readStatusService.findById(readStatusId));
  }
}
