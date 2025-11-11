package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContent")
@RequiredArgsConstructor
public class BinaryContentController {

    private final BinaryContentRepository binaryContentRepository;

    // ✅ BinaryContent 조회 (심화 요구사항)
    @GetMapping("/find")
    public ResponseEntity<BinaryContent> find(@RequestParam UUID binaryContentId) {
        return binaryContentRepository.findById(binaryContentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
