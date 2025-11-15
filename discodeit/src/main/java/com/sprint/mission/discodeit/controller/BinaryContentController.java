package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContents")
@Tag(name = "BinaryContent", description = "파일 데이터 관련 API")
public class BinaryContentController {

  private final BinaryContentService binaryContentService;

  public BinaryContentController(BinaryContentService binaryContentService) {
    this.binaryContentService = binaryContentService;
  }

  // ✅ 1️⃣ 단일 파일 다운로드
  @GetMapping("/{id}")
  public ResponseEntity<byte[]> getBinaryContent(@PathVariable UUID id) {
    // 🔹 BinaryContent 조회
    BinaryContent content = binaryContentService.find(id);

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + content.getFileName() + "\"")
        .contentType(MediaType.parseMediaType(content.getContentType()))
        .body(content.getBytes());
  }

  // ✅ 2️⃣ 여러 파일 조회 (단순 리스트)
  @Operation(summary = "여러 파일 조회")
  @GetMapping
  public ResponseEntity<List<BinaryContentDto>> getBinaryContents(
      @RequestParam List<UUID> ids) {
    var contents = binaryContentService.findAllByIdIn(ids);
    var responses = contents.stream()
        .map(BinaryContentDto::from)
        .toList();

    return ResponseEntity.ok(responses);
  }
}
