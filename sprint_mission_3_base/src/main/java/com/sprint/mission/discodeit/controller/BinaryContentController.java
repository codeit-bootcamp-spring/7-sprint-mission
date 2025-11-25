package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binary.BinaryContentDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/binary-content")
public class BinaryContentController {

    private final BinaryContentService binaryContentService;
    private final BinaryContentStorage storage;

    /** 이미지 다운로드 */
    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> download(@PathVariable UUID id) {
        BinaryContentDto dto = binaryContentService.find(id);
        return storage.download(dto);
    }
}
