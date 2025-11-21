package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.openapi.BinaryContentControllerDocs;
import com.sprint.mission.discodeit.dto.binarycontent.Response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/binaryContents")
public class BinaryContentController implements BinaryContentControllerDocs {
    private final BinaryContentService binaryContentService;

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
    public ResponseEntity<byte[]> download(@PathVariable UUID binaryContentId) {
        BinaryContentResponseDto binaryContent = binaryContentService.find(binaryContentId);
        return ResponseEntity.status(HttpStatus.OK).body(binaryContent.bytes());
    }
}
