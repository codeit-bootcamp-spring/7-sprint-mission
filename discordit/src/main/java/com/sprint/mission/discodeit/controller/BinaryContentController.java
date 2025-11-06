package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.request.BinaryContentGetRequest;
import com.sprint.mission.discodeit.dto.binaryContent.response.BinaryContentResponse;
import com.sprint.mission.discodeit.service.BinaryContentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContent")
@RequiredArgsConstructor
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    @RequestMapping(value = "/find", method = RequestMethod.POST)
    public ResponseEntity<BinaryContentResponse> getContent(@Valid @RequestBody UUID binaryContentId) {
        return ResponseEntity.ok(binaryContentService.get(binaryContentId));
    }

    @RequestMapping(value = "/findAll", method = RequestMethod.POST)
    public ResponseEntity<List<BinaryContentResponse>> getContent(@Valid @RequestBody BinaryContentGetRequest request) {
        return ResponseEntity.ok(binaryContentService.getAllById(request));
    }
}
