package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.dto.response.BinaryContentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Slf4j
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    @GetMapping
    public List<BinaryContentDto> getFile(@RequestParam List<String> binaryContentId) {

        return binaryContentService.getBinaryContents(binaryContentId);
    }

    @GetMapping("/{binaryContentId}")
    public BinaryContentDto getFile(@PathVariable String binaryContentId) {
        return binaryContentService.getBinaryContent(binaryContentId);
    }


}
