package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.Docs.BinaryContentServiceDocs;
import com.sprint.mission.discodeit.dto.Binarycontent.response.BinaryContentDto;
import com.sprint.mission.discodeit.entity.content.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController implements BinaryContentServiceDocs {

    private final BinaryContentService binaryContentService;
    private final BinaryContentStorage binaryContentStorage;


    @RequestMapping(path = "{binaryContentId}", method = RequestMethod.GET)
    public ResponseEntity<BinaryContentDto> find(@PathVariable UUID binaryContentId) {

        BinaryContentDto binaryContent = binaryContentService.find(binaryContentId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(binaryContent);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<BinaryContentDto>> findAllByIdIn(
            @RequestParam("binaryContentId") List<UUID> binaryContentId) {

        List<BinaryContentDto> binaryContents = binaryContentService.findAllByIdIn(binaryContentId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(binaryContents);
    }


    @GetMapping("/{binaryContentId}/download")
    public ResponseEntity<?> download(@PathVariable UUID binaryContentId) {

        return binaryContentService.download(binaryContentId);
    }


}


