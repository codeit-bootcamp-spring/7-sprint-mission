package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.notification.NotificationResponseDto;
import com.sprint.mission.discodeit.global.config.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationResponseDto>> getNotifications(@AuthenticationPrincipal DiscodeitUserDetails userDetails) {
        UUID receiverId = userDetails.getUserResponseDto().id();
        List<NotificationResponseDto> notifications = notificationService.getAllNotificationsByReceiverId(receiverId);
        return ResponseEntity.ok(notifications);
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(@PathVariable UUID notificationId, @AuthenticationPrincipal DiscodeitUserDetails userDetails) {
        UUID receiverId = userDetails.getUserResponseDto().id();
        notificationService.deleteNotification(notificationId, receiverId);
        return ResponseEntity.noContent().build();
    }


}
