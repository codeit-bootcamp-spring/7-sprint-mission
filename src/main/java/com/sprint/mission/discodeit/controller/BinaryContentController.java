package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.docs.BinaryContentControllerDocs;
import com.sprint.mission.discodeit.dto.response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.InputStream;
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
  private final BinaryContentStorage storage;

  //심화. 바이너리 파일 1개 조회
  @GetMapping(value = "/{binaryContentId}")
  public ResponseEntity<BinaryContentResponseDto> findByBinaryContent(
      @PathVariable UUID binaryContentId) {
    BinaryContentResponseDto response = binaryContentService.find(binaryContentId);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  //여러개 조회
  @GetMapping
  public ResponseEntity<List<BinaryContentResponseDto>> findAllByBinaryContent(
      @RequestParam List<UUID> binaryContentIds) {
    List<BinaryContentResponseDto> response = binaryContentService.findAllByIdIn(binaryContentIds);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @GetMapping(value = "/{binaryContentId}/download")
  public ResponseEntity<?> download(
      @PathVariable UUID binaryContentId) {
    BinaryContentResponseDto binaryContent = binaryContentService.find(binaryContentId);
    return storage.download(binaryContent);

  }
}
