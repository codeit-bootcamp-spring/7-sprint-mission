package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/binaryContents")
public class BinaryContentController {

  private final BinaryContentService binaryContentService;

  /**
   * 단일 BinaryContent 조회 API
   * GET /api/binaryContents/{binaryContentId}
   * - path variable로 조회할 BinaryContent ID를 받고,
   * - 해당 BinaryContent 엔티티를 반환한다.
   */
  @RequestMapping(
      method = RequestMethod.GET,
      value = "/{binaryContentId}",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<BinaryContent> find(@PathVariable("binaryContentId") UUID binaryContentId) {
    BinaryContent binaryContent = binaryContentService.find(binaryContentId);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(binaryContent);
  }

  /**
   * 여러 BinaryContent 조회 API
   * GET /api/binaryContents?binaryContentIds={id1}&binaryContentIds={id2}...
   * - query parameter로 BinaryContent ID 목록을 받고,
   * - 해당하는 BinaryContent 리스트를 반환한다.
   */
  @RequestMapping(
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<List<BinaryContent>> findAllByIdIn(
      @RequestParam("binaryContentIds") List<UUID> binaryContentIds
  ) {
    List<BinaryContent> binaryContents = binaryContentService.findAllByIdIn(binaryContentIds);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(binaryContents);
  }
}
