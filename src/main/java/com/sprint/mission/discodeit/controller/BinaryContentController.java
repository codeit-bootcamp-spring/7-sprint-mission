package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.dto.response.BinaryContentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> getFile(@RequestParam List<UUID> binaryContentId) {

        List<BinaryContentDto> binaryContents = binaryContentService.getBinaryContents(binaryContentId);
        return ResponseEntity.status(HttpStatus.OK).body(binaryContents);
    }

    @GetMapping("/{binaryContentId}")
    public ResponseEntity<BinaryContentDto> getFile(@PathVariable UUID binaryContentId) {
        BinaryContentDto binaryContent = binaryContentService.getBinaryContent(binaryContentId);
        return ResponseEntity.status(HttpStatus.OK).body(binaryContent);
    }

    @GetMapping("/{binaryContentId}/download")
    public ResponseEntity<UrlResource> download(@PathVariable UUID binaryContentId){
        return binaryContentService.getUrl(binaryContentId);
       
         
    }


}
