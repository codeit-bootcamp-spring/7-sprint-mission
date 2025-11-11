package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/binary-contents")
@RequiredArgsConstructor
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    @RequestMapping(method= RequestMethod.GET, value = "/{binaryContentId}")
    public ResponseEntity<Resource> getBinaryContent(@PathVariable UUID binaryContentId){
        BinaryContent binaryContent = binaryContentService.findById(binaryContentId);
        ByteArrayResource resource = new ByteArrayResource(binaryContent.getData());

        log.info("파일 다운로드: {}", binaryContentId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(binaryContent.getFileType()))
                .header("Content-Disposition", "attachment; filename=\""
                        + binaryContent.getFileName() + "\"")
                .contentLength(resource.contentLength())
                .body(resource);
    }
}
