package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.Docs.BinaryContentServiceDocs;
import com.sprint.mission.discodeit.dto.Binarycontent.response.BinaryContentDto;
import com.sprint.mission.discodeit.entity.content.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
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
        System.out.println("여기냐2");
        List<BinaryContentDto> binaryContents = binaryContentService.findAllByIdIn(binaryContentId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(binaryContents);
    }


}


