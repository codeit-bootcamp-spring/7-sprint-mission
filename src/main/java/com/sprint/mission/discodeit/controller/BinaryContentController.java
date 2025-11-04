package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/binarycontent")
public class BinaryContentController {
    private final UserService userService;
    private final MessageService messageService;
    private final BinaryContentService binaryContentService;

    // 유저 프로필 이미지 조회
    @RequestMapping(value = "/search/profile/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> searchProfile(@PathVariable UUID userId) {
        BinaryContent content;

        try {
            UserResponseDto user = userService.find(userId);
            content = binaryContentService.find(user.getProfileId());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok(content);
    }

    // 메시지 파일 조회
    @RequestMapping(value = "/search/file/{messageId}", method = RequestMethod.GET)
    public ResponseEntity<?> searchFile(@PathVariable UUID messageId) {
        List<BinaryContent> content;

        try {
            Message message = messageService.find(messageId);
            content = binaryContentService.findAllByIdIn(message.getAttachmentIds());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok(content);
    }
}
