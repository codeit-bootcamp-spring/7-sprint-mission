package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.docs.BinaryContentControllerDocs;
import com.sprint.mission.discodeit.dto.binarycontent.response.BinaryContentInfoRes;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/binary-contents")
@RequiredArgsConstructor
public class BinaryContentController implements BinaryContentControllerDocs {

  private final BinaryContentService binaryContentService;
  private final BinaryContentStorage binaryContentStorage;

  //단일 파일 조회
  @GetMapping("/{binaryContentId}")
  public ResponseEntity<BinaryContentInfoRes> getFileInfo(@PathVariable UUID binaryContentId) {
    return ResponseEntity.ok(binaryContentService.getBinaryContent(binaryContentId));
  }

  //BinaryId 여러개로 조회
  @GetMapping
  public ResponseEntity<List<BinaryContentInfoRes>> getAllFilesInfo(
      @RequestParam List<UUID> binaryContentIdList) {
    return ResponseEntity.ok(binaryContentService.getBinaryContentList(binaryContentIdList));
  }

  //단일 파일 다운로드
  @GetMapping("/{binaryContentId}/download")
  public ResponseEntity<BinaryContentInfoRes> downloadFile(@PathVariable UUID binaryContentId) {
    BinaryContentInfoRes res = BinaryContentMapper.toResDto(
        binaryContentService.findById(binaryContentId));
    binaryContentStorage.download(res);
    return ResponseEntity.ok(res);
  }
}