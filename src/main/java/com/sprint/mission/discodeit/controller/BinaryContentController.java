package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BinaryContentController {
    private final UserService userService;
    private final MessageService messageService;
    private final BinaryContentService binaryContentService;

    // 유저 프로필 이미지 조회
    @RequestMapping(value = "/binarycontent/profile", method = RequestMethod.GET)
    public ResponseEntity<?> searchProfile(@RequestParam UUID userId) {
        UserResponseDto user = userService.find(userId);
        BinaryContent content = binaryContentService.find(user.getProfileId());
        return ResponseEntity.ok(content);
    }

    // 메시지 파일 조회
    @RequestMapping(value = "/binarycontent/file", method = RequestMethod.GET)
    public ResponseEntity<?> searchFile(@RequestParam UUID messageId) {
        Message message = messageService.find(messageId);
        List<BinaryContent> content = binaryContentService.findAllByIdIn(message.getAttachmentIds());
        return ResponseEntity.ok(content);
    }
}
