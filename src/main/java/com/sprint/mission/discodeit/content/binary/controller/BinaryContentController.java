package com.sprint.mission.discodeit.content.binary.controller;

import com.sprint.mission.discodeit.content.binary.BinaryContent;
import com.sprint.mission.discodeit.content.binary.BinaryContentService;
import com.sprint.mission.discodeit.content.binary.dto.BinaryContentResTemp;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController {

  private final BinaryContentService binaryContentService;

  @GetMapping
  public ResponseEntity<?> getBinaryContents(
      @RequestBody List<String> ids
  ) {
    List<BinaryContentResTemp> contentResTemps = ids.stream().map(UUID::fromString)
        .map(binaryContentService::findById)
        .map(BinaryContentResTemp::from)
        .toList();
    return ResponseEntity.ok(contentResTemps);
  }

  @GetMapping("/{binaryContentId}")
  public ResponseEntity<?> getBinaryContentsByPath(@PathVariable String binaryContentId) {
    UUID id = UUID.fromString(binaryContentId);
    BinaryContent binaryContent = binaryContentService.findById(id);
    return ResponseEntity.ok(BinaryContentResTemp.from(binaryContent));
  }


}
