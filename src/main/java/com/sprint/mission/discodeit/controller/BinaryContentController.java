package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContentDto.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
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

    // 단일조회
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<BinaryContentDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(binaryContentService.findBinaryContentById(id));
    }

    // 하나 및 여러 개 조회
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<BinaryContentDto>> getAllByIdIn(@RequestParam List<UUID> binaryContentIds) {
        return ResponseEntity.ok(binaryContentService.findAllBinaryContentByIdIn(binaryContentIds));
    }

    @RequestMapping(value = "/{id}/download", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadById(@PathVariable UUID id) {
        BinaryContentDto dto = binaryContentService.findBinaryContentById(id);

        return binaryContentStorage.download(dto);
    }
}
