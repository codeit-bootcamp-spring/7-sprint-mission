package com.sprint.mission.discodeit.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import com.sprint.mission.discodeit.entity.dto.Res_BinaryContent;
import com.sprint.mission.discodeit.service.basic.BinaryContentService;
import java.util.ArrayList;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ResponseBody
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    //💎 여러 첨부 파일 조회
    @RequestMapping(method = GET)
    public ResponseEntity<ArrayList<Res_BinaryContent>> find(
        UUID[] binaryContentIds) {
        ArrayList<Res_BinaryContent> arrayList = new ArrayList<>(binaryContentService.findAllByIdIn(binaryContentIds));
        return ResponseEntity
              .status(HttpStatus.OK)
              .body(arrayList);
    }

    //💎 첨부 파일 조회
    @RequestMapping(value = "/{binaryContentId}", method = GET)
    public ResponseEntity<Res_BinaryContent> find(
        @PathVariable("binaryContentId") UUID binaryContentId) {
        Res_BinaryContent resBinaryContent = binaryContentService.find(binaryContentId);
        return ResponseEntity
              .status(HttpStatus.OK)
              .body(resBinaryContent);
    }
}
