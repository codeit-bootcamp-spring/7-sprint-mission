package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.dto.Res_BinaryContent;
import com.sprint.mission.discodeit.service.basic.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/api/binaryContent")
@RequiredArgsConstructor
public class BinaryContentController {
    private final BinaryContentService binaryContentService;
    //[ ]  BinaryContentModel 파일 조회
    //    url: /api/binaryContent/find
    //    파라미터: binaryContentId
    //    바디 없음
    //    응답: ResponseEntity<BinaryContentModel>
    @RequestMapping(value = "/find", method = GET)
    public ResponseEntity<Res_BinaryContent> find(UUID binaryContentId) {
        return ResponseEntity.ok(binaryContentService.find(binaryContentId));
    }
}
