package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.docs.BinaryContentControllerDocs;
import com.sprint.mission.discodeit.dto.response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController implements BinaryContentControllerDocs {

  private final BinaryContentService binaryContentService;

  //심화. 바이너리 파일 1개 조회
  @GetMapping(value = "/{binaryContentId}")
  public ResponseEntity<BinaryContent> findByBinaryContent(
      @PathVariable UUID binaryContentId) {
    BinaryContent response = binaryContentService.find(binaryContentId);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  //여러개 조회
  @GetMapping
  public ResponseEntity<List<BinaryContent>> findAllByBinaryContent(
      @RequestParam List<UUID> binaryContentIds) {
    List<BinaryContent> response = binaryContentService.findAllByIdIn(binaryContentIds);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
