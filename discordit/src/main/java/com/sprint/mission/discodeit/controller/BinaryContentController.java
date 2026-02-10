package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.entity.binaryContent.BinaryContentDto;
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

    @GetMapping
    public ResponseEntity<List<BinaryContentDto>> getContents(@RequestParam List<UUID> binaryContentIds) {
        return ResponseEntity.ok(binaryContentService.getByIds(binaryContentIds));
    }

    @GetMapping("/{binaryContentId}")
    public ResponseEntity<BinaryContentDto> getContent(@PathVariable UUID binaryContentId) {
        return ResponseEntity.ok(binaryContentService.get(binaryContentId));
    }
    @GetMapping("/{binaryContentId}/download")
    public ResponseEntity<?> download(@PathVariable UUID binaryContentId) {
        return binaryContentStorage.download(binaryContentService.get(binaryContentId));
    }
}
