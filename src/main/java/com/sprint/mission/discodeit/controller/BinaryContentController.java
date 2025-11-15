package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binarycontent.response.BinaryContentInfoRes;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binary-contents")
@RequiredArgsConstructor
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    //단일 파일 조회
    @RequestMapping(method= RequestMethod.GET, value = "/{binaryContentId}")
    public ResponseEntity<BinaryContentInfoRes> getFileInfo(@PathVariable UUID binaryContentId){
        return ResponseEntity.ok(binaryContentService.getBinaryContent(binaryContentId));
    }

    //모든 파일 조회
    @RequestMapping(method= RequestMethod.GET)
    public ResponseEntity<List<BinaryContentInfoRes>> getAllFilesInfo(){
        return ResponseEntity.ok(binaryContentService.getBinaryContentList());
    }
}