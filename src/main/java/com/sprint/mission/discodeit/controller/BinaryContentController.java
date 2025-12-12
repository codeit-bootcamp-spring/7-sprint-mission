package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.openapi.BinaryContentControllerDocs;
import com.sprint.mission.discodeit.dto.binarycontent.Response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/binaryContents")
public class BinaryContentController implements BinaryContentControllerDocs {
    private final BinaryContentService binaryContentService;
    private final BinaryContentStorage binaryContentStorage;

    @GetMapping("/{binaryContentId}")
    public ResponseEntity<BinaryContentResponseDto> find(@PathVariable UUID binaryContentId) {
        BinaryContentResponseDto binaryContent = binaryContentService.find(binaryContentId);
        return ResponseEntity.status(HttpStatus.OK).body(binaryContent);
    }

    @GetMapping
    public ResponseEntity<List<BinaryContentResponseDto>> findAllByIdIn(@RequestParam List<UUID> binaryContentIds) {
        List<BinaryContentResponseDto> binaryContents = binaryContentService.findAllByIdIn(binaryContentIds);
        return ResponseEntity.status(HttpStatus.OK).body(binaryContents);
    }

    @GetMapping("/{binaryContentId}/download")
    public ResponseEntity<?> download(@PathVariable UUID binaryContentId) {
        log.info("GET /api/binaryContents/{}/download - 파일 다운로드 요청", binaryContentId);
        BinaryContentResponseDto binaryContent = binaryContentService.find(binaryContentId);
        return binaryContentStorage.download(binaryContent);
    }
}
