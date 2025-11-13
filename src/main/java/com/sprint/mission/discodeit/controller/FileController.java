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
public class FileController implements FileControllerDocs {

    private final BinaryContentService binaryContentService;
    @RequestMapping(value = "/{binaryContentId}", method = RequestMethod.GET)
    @Override
    public ResponseEntity<BinaryContentResponseDto> read(@PathVariable UUID binaryContentId){
        return new ResponseEntity<>(binaryContentService.find(binaryContentId), HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @Override
    public ResponseEntity<List<BinaryContentResponseDto>> readByIdList(@RequestParam List<UUID> binaryContentIds){
        return new ResponseEntity<>(binaryContentService.findAllByIdIn(binaryContentIds), HttpStatus.OK);
    }

    @RequestMapping(value = "",method = RequestMethod.OPTIONS)
    public ResponseEntity<List<BinaryContent>> readAll(){
        return new ResponseEntity<>(binaryContentService.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    public void reset(){
        binaryContentService.resetBinaryContentService();
    }

/// /////////////////////////////////////////////////////////////////////////////////////////////////////

}
