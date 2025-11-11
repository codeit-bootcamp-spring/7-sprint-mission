package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
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
public class FileController {

    private final BinaryContentService binaryContentService;
    @RequestMapping(value = "/{binaryContentId}", method = RequestMethod.GET)
    public ResponseEntity<BinaryContent> read(@PathVariable UUID binaryContentId){
        return new ResponseEntity<>(binaryContentService.find(binaryContentId), HttpStatus.OK);
    }

    @RequestMapping(value = "",method = RequestMethod.GET)
    public ResponseEntity<List<BinaryContentResponseDto>> readByIdList(@RequestParam List<UUID> binaryContentIds){
        return new ResponseEntity<>(binaryContentService.findAllByIdIn(binaryContentIds), HttpStatus.OK);
    }
/// /////////////////////////////////////////////////////////////////////////////////////////////////////
//    @RequestMapping("/reset")
//    public void reset(){
//        binaryContentService.resetBinaryContentService();
//    }
}
