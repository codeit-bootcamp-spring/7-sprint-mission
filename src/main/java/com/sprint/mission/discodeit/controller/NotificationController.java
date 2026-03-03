package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.NotificationService;
import com.sprint.mission.discodeit.service.dto.response.NotificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/api/notifications")
    public ResponseEntity<List<NotificationDto>> notifications(
            @AuthenticationPrincipal DiscodeitUserDetails userDetails
    ){
        UUID receiverId = userDetails.getUserDto().getId();
        List<NotificationDto> result = notificationService.getNotifications(receiverId);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/api/notifications/{notificationId}")
    public ResponseEntity<Void> checkNotification(
            @PathVariable UUID notificationId,
            @AuthenticationPrincipal DiscodeitUserDetails userDetails
    ) {
        UUID requesterId = userDetails.getUserDto().getId();
        notificationService.checkNotification(notificationId, requesterId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
