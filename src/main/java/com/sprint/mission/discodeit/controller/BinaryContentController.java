package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binary-content")
@RequiredArgsConstructor
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    //바이너리 파일 1개 조회
    @RequestMapping(value = "{BinaryContentId}", method = RequestMethod.GET)
    public ResponseEntity<BinaryContentResponseDto> findByBinaryContent(@PathVariable UUID BinaryContentId) {
        BinaryContentResponseDto response = binaryContentService.find(BinaryContentId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //여러개 조회
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<BinaryContentResponseDto>>findAllByBinaryContent(@RequestParam List<UUID> Ids) {
        List<BinaryContentResponseDto> response = binaryContentService.findAllByIdIn(Ids);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
