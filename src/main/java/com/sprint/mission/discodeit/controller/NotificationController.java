package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readstatus.response.NotificationDto;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.basic.BasicNotificationService;
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

    private final BasicNotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationDto>> getAllNotifications(@AuthenticationPrincipal DiscodeitUserDetails userDetails) {
        List<NotificationDto> notificationDtoList = notificationService.findAll(userDetails);
        return ResponseEntity.ok(notificationDtoList);
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(
            @PathVariable UUID notificationId,
            @AuthenticationPrincipal DiscodeitUserDetails userDetails
    ) {
        notificationService.delete(userDetails.getUserDto().id(), notificationId);
        return ResponseEntity.noContent().build();
    }

}
