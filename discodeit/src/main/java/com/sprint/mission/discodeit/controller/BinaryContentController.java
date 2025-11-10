package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/binary-contents")
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    public BinaryContentController(BinaryContentService binaryContentService) {
        this.binaryContentService = binaryContentService;
    }

    // ✅ 1️⃣ 단일 파일 다운로드
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
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
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<BinaryContent>> getBinaryContents(@RequestParam List<UUID> ids) {
        List<BinaryContent> contents = binaryContentService.findAllByIdIn(ids);
        return ResponseEntity.ok(contents);
    }
}
