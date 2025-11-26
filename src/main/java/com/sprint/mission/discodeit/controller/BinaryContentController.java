package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@Slf4j
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    @GetMapping
    public List<BinaryContentDto> getFile(@RequestParam List<UUID> binaryContentId) {

        List<BinaryContent> binaryContents = binaryContentService.getBinaryContents(binaryContentId);
        return null;
    }

    @GetMapping("/{binaryContentId}")
    public BinaryContentDto getFile(@PathVariable UUID binaryContentId) {
        binaryContentService.getBinaryContent(binaryContentId);
        return null;
    }


}
