package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.docs.BinaryContentControllerDocs;
import com.sprint.mission.discodeit.dto.response.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController implements BinaryContentControllerDocs {

    private final BinaryContentService binaryContentService;
    private final BinaryContentStorage binaryContentStorage;
    @GetMapping( "/{binaryContentId}")
    @Override
    public ResponseEntity<BinaryContentDto> read(@PathVariable UUID binaryContentId){
        return new ResponseEntity<>(binaryContentService.find(binaryContentId), HttpStatus.OK);
    }

    @GetMapping("")
    @Override
    public ResponseEntity<List<BinaryContentDto>> readByIdList(@RequestParam List<UUID> binaryContentIds){
        return new ResponseEntity<>(binaryContentService.findAllByIdIn(binaryContentIds), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<BinaryContent>> readAll(){
        return new ResponseEntity<>(binaryContentService.findAll(), HttpStatus.OK);
    }

    @PostMapping("/reset")
    public void reset(){
        binaryContentService.resetBinaryContentService();
    }

    @GetMapping("/{binaryContentId}/download")
    public ResponseEntity<?> downloadBinaryContent(@PathVariable UUID binaryContentId) throws IOException {

        return binaryContentService.downloadFile(binaryContentId);
    }


}
