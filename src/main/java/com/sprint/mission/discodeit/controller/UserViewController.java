package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserViewController {
    private final UserService userService;
    private final BinaryContentService binaryContentService;

    // 유저 목록 조회
    @RequestMapping(value = "/api/user/findAll", method = RequestMethod.GET)
    public ResponseEntity<List<UserResponseDto>> searchUsers() {
        return ResponseEntity.ok().body(userService.findAll());
    }

    // binaryContentId로 파일 찾기
    @RequestMapping(value = "/api/binaryContent/find", method = RequestMethod.GET)
    public ResponseEntity<?> searchProfileById(@RequestParam UUID binaryContentId) {
        BinaryContent content = binaryContentService.find(binaryContentId);
        return ResponseEntity.ok(content);
    }
}
