package com.sprint.mission.discodeit.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import com.sprint.mission.discodeit.swaggerDocs.BinaryContentDoc;
import com.sprint.mission.discodeit.entity.dto.Res_BinaryContent;
import com.sprint.mission.discodeit.service.basic.BinaryContentService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController //👍 @controller + @responsebody
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController implements BinaryContentDoc {
    private final BinaryContentService binaryContentService;

    //💎 여러 첨부 파일 조회
    @GetMapping
    public ResponseEntity<List<Res_BinaryContent>> find(
        @RequestBody UUID[] binaryContentIds) {

        List<Res_BinaryContent> arrayList
            = new ArrayList<>(binaryContentService.findAllByIdIn(binaryContentIds));

        return ResponseEntity
              .status(HttpStatus.OK)
              .body(arrayList);
    }

    //💎 첨부 파일 조회
    @GetMapping("/{binaryContentId}")
    public ResponseEntity<Res_BinaryContent> find(
        @PathVariable("binaryContentId") UUID binaryContentId) {

        Res_BinaryContent resBinaryContent
            = binaryContentService.find(binaryContentId);

        return ResponseEntity
              .status(HttpStatus.OK)
              .body(resBinaryContent);
    }
}
