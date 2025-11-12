package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.dto.response.BinaryContentResponse;
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
    public List<BinaryContentResponse> getFile(@RequestParam List<UUID> binaryContentId) {

        return binaryContentService.getBinaryContents(binaryContentId);
    }

    @GetMapping("/{binaryContentId}")
    public BinaryContentResponse getFile(@PathVariable UUID binaryContentId) {
        return binaryContentService.getBinaryContent(binaryContentId);
    }


//    @GetMapping
//    public ResponseEntity<List<Resource>> getFiles(@RequestParam List<UUID> ids){
//        List<Resource> result = new ArrayList<>();
//        for (UUID id : ids) {
//            result.add(binaryContentService.getImageFile(id));
//        }
//        return ResponseEntity.ok(result);
//    }

}
