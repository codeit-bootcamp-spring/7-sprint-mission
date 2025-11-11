package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.dto.binaryContentDto.BinaryContentResponseDto;
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
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    // 단일조회
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<BinaryContentResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(binaryContentService.findBinaryContentById(id));
    }

    // 하나 및 여러 개 조회
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<BinaryContentResponseDto>> getAllByIdIn(@RequestParam List<UUID> ids) {
        return ResponseEntity.ok(binaryContentService.findAllBinaryContentByIdIn(ids));
    }
}
