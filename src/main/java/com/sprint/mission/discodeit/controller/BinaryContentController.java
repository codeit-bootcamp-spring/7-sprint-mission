package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.Docs.BinaryContentServiceDocs;
import com.sprint.mission.discodeit.dto.Binarycontent.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.Binarycontent.request.BinaryContentFindByIdRequest;
import com.sprint.mission.discodeit.dto.Binarycontent.response.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.content.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController implements BinaryContentServiceDocs {

    private final BinaryContentService binaryContentService;


    @RequestMapping(path = "{binaryContentId}", method = RequestMethod.GET)
    public ResponseEntity<BinaryContent> find(@PathVariable UUID binaryContentId) {

        BinaryContent binaryContent = binaryContentService.find(binaryContentId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(binaryContent);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<BinaryContent>> findAllByIdIn(
            @RequestParam("binaryContentId") List<UUID> binaryContentId) {
        System.out.println("여기냐2");
        List<BinaryContent> binaryContents = binaryContentService.findAllByIdIn(binaryContentId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(binaryContents);
    }


}


