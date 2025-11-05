package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.request.BinaryContentGetRequest;
import com.sprint.mission.discodeit.dto.binaryContent.response.BinaryContentResponse;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController("/contents")
@RequiredArgsConstructor
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<List<BinaryContentResponse>> getContent(BinaryContentGetRequest request) {
        return ResponseEntity.ok(binaryContentService.get(request));
    }
}
