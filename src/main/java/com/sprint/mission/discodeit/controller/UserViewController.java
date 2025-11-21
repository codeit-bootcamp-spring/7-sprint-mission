package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

// 스프린트 미션 4 : HTML,CSS,JS를 활용한 화면 서빙에서만 사용되는 API입니다.
// swagger에 보이지 않도록 @Hidden으로 제외시켰습니다.
@Hidden
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserViewController {
    private final UserService userService;
    private final BinaryContentService binaryContentService;

    // 유저 목록 조회
    @GetMapping("/user/findAll")
    public ResponseEntity<List<UserResponseDto>> searchUsers() {
        return ResponseEntity.ok().body(userService.findAll());
    }

    // binaryContentId로 파일 찾기
    @GetMapping("/binaryContent/find")
    public ResponseEntity<BinaryContent> searchProfileById(@RequestParam UUID binaryContentId) {
        BinaryContent content = binaryContentService.find(binaryContentId);
        return ResponseEntity.ok(content);
    }
}
