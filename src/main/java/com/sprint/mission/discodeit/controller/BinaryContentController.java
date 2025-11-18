package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController {

  private final BinaryContentService binaryContentService;

  // 단일 조회: PathVariable 사용
  @GetMapping("/{binaryContentId}")
  public ResponseEntity<BinaryContentResponseDto> getBinaryContent(
      @PathVariable UUID binaryContentId) {
    BinaryContentResponseDto binaryContent = binaryContentService.getBinaryContent(binaryContentId);
    return ResponseEntity.ok(binaryContent);
  }

  // 다중 조회: RequestParam List 사용
  @GetMapping
  public ResponseEntity<List<BinaryContentResponseDto>> getAllBinaryContent(
      @RequestParam(required = false) List<UUID> binaryContentIds) {
    List<BinaryContentResponseDto> dtos = binaryContentService.getAllBinaryContentByIdIn(
        binaryContentIds);
    return ResponseEntity.ok(dtos);
  }
}

