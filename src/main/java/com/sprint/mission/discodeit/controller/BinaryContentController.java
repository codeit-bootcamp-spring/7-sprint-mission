package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.global.dto.ApiResponse;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.websocket.Decoder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @RequestMapping(value = "/binaryContents/{binaryContentId}", method = RequestMethod.GET)
    public ResponseEntity<BinaryContent> find(@PathVariable UUID binaryContentId) {
        BinaryContent binaryContent = binaryContentService.find(binaryContentId);
        return ResponseEntity.status(HttpStatus.OK).body(binaryContent);
    }

    @RequestMapping(value = "/binaryContents", method = RequestMethod.GET)
    public ResponseEntity<List<BinaryContent>> findAllByIdIn(@RequestParam List<UUID> binaryContentIds) {
        List<BinaryContent> binaryContents = binaryContentService.findAllByIdIn(binaryContentIds);
        return ResponseEntity.status(HttpStatus.OK).body(binaryContents);
    }

    //--------------------- 기존의 메서드(심화 요구사항에서 사용하지 않아 변경X) ---------------------//
    // 유저 프로필 이미지 조회
    @RequestMapping(value = "/binarycontent/profile", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<BinaryContent>> searchProfile(@RequestParam UUID userId) {
        UserResponseDto user = userService.find(userId);
        BinaryContent content = binaryContentService.find(user.getProfileId());
        return ApiResponse.success(HttpStatus.OK, "프로필 이미지 조회", content);
    }

    // 메시지 파일 조회
    @RequestMapping(value = "/binarycontent/file", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<List<BinaryContent>>> searchFile(@RequestParam UUID messageId) {
        Message message = messageService.find(messageId);
        List<BinaryContent> content = binaryContentService.findAllByIdIn(message.getAttachmentIds());
        return ApiResponse.success(HttpStatus.OK, "메시지 파일 조회", content);
    }
}
