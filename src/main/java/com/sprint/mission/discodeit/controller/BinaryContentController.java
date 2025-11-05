package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * [ ] 바이너리 파일을 1개 또는 여러 개 조회할 수 있다. "/api/binaryContent, POST
 *
 */
@RestController
@RequestMapping("api/binaryContent")
@RequiredArgsConstructor
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    // RequestParam이나 RequestBody로는 UUID를 보낼 수 없음.
    // 따라서 POST 방식으로 전송
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> getAllBinaryContent(@RequestBody List<UUID> binaryContentIds) {
        List<BinaryContentResponseDto> binaryContentResponseDtos = binaryContentService.getAllBinaryContentByIdIn(binaryContentIds);
        return  ResponseEntity.ok(binaryContentResponseDtos);
    }
}
