package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController {

    private final BinaryContentService binaryContentService;
    private final BinaryContentStorage binaryContentStorage;

    // 단일 조회: PathVariable 사용
    @GetMapping("/{binaryContentId}")
    public ResponseEntity<BinaryContentResponseDto> getBinaryContent(
            @PathVariable UUID binaryContentId) {
        BinaryContentResponseDto responseDto = binaryContentService.getBinaryContent(binaryContentId);
        return ResponseEntity.ok(responseDto);
    }

    // 다중 조회: RequestParam List 사용
    @GetMapping
    public ResponseEntity<List<BinaryContentResponseDto>> getAllBinaryContent(
            @RequestParam(required = false) List<UUID> binaryContentIds) {
        List<BinaryContentResponseDto> responseDtos = binaryContentService.getAllBinaryContentByIdIn(
                binaryContentIds);
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/{binaryContentId}/download")
    public ResponseEntity<?> downloadBinaryContent(@PathVariable UUID binaryContentId) {
        BinaryContentResponseDto responseDto = binaryContentService.getBinaryContent(binaryContentId);
        return binaryContentStorage.download(responseDto);
    }
}

