package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.doc.BinaryContentDocs;
import com.sprint.mission.discodeit.dto.binaryContent.response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController implements BinaryContentDocs {

    private final BinaryContentService binaryContentService;

    @RequestMapping(value = "/{binaryContentId}", method = RequestMethod.GET)
    public ResponseEntity<BinaryContentResponseDto> getBinaryContent(@PathVariable UUID binaryContentId) {
        BinaryContentResponseDto binaryContent = binaryContentService.getBinaryContent(binaryContentId);
        return ResponseEntity.status(HttpStatus.OK).body(binaryContent);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<BinaryContentResponseDto>> getAllBinaryContent(@RequestParam List<UUID> binaryContentIds) {
        List<BinaryContentResponseDto> binaryContentResponseDtos = binaryContentService.getAllBinaryContentByIdIn(binaryContentIds);
        return ResponseEntity.status(HttpStatus.OK).body(binaryContentResponseDtos);
    }
}
