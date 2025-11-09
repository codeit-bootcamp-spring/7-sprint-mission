package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.global.util.ApiResponse;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * [ ] 바이너리 파일을 1개 또는 여러 개 조회할 수 있다. "api/binaryContent/{binaryContentIds}, POST
 */
@RestController
@RequestMapping("api/binaryContent")
@RequiredArgsConstructor
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<BinaryContentResponseDto> getBinaryContent(@RequestParam UUID binaryContentId) {
        BinaryContentResponseDto binaryContent = binaryContentService.getBinaryContent(binaryContentId);
        return ResponseEntity.status(HttpStatus.OK).body(binaryContent);
    }

    // RequestParam이나 RequestBody로는 UUID를 보낼 수 없음. UUID가 너무 길기 때문
    // 하지만 read 로직에서 POST는 올바르지 않은거 같음
    // 따라서 PathVariable로 /api/binaryContent/{uuid1,uuid2,uuid3...}로 결정
    // binaryContentIds = [uuid1, uuid2, uuid3]
    @RequestMapping(value = "/{binaryContentIds}", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<List<BinaryContentResponseDto>>> getAllBinaryContent(@PathVariable List<UUID> binaryContentIds) {
        List<BinaryContentResponseDto> binaryContentResponseDtos = binaryContentService.getAllBinaryContentByIdIn(binaryContentIds);
        ApiResponse<List<BinaryContentResponseDto>> responseBody = ApiResponse.success(binaryContentResponseDtos);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
