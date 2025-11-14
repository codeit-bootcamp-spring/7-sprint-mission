package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binarycontent.response.BinaryContentInfoRes;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
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