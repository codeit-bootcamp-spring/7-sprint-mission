package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.dto.response.notification.NotificationDto;
import com.sprint.mission.discodeit.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationDto>> getAll(
            @AuthenticationPrincipal DiscodeitUserDetails principal
    ) {
        UUID requesterId = principal.getUserDto().id();
        return ResponseEntity.ok(notificationService.getAllByReceiverId(requesterId));
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(
            @PathVariable UUID notificationId,
            @AuthenticationPrincipal DiscodeitUserDetails principal
    ) {
        UUID requesterId = principal.getUserDto().id();
        notificationService.delete(notificationId, requesterId);
        return ResponseEntity.noContent().build();
    }

}
