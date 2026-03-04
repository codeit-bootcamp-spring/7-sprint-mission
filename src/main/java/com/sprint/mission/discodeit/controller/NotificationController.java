package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readStatusDto.NotificationDto;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationDto>> getNotifications(
            @AuthenticationPrincipal DiscodeitUserDetails userDetails) {

        return ResponseEntity.ok(notificationService.findAllNotifications(userDetails.getUserDto().id()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(
            @AuthenticationPrincipal DiscodeitUserDetails userDetails, @PathVariable UUID id) {
        notificationService.deleteNotification(id, userDetails.getUserDto().id());
        return ResponseEntity.noContent().build();
    }
}
