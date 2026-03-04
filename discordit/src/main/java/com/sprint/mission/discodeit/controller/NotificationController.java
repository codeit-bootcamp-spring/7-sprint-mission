package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.DiscodeitUserDetails;
import com.sprint.mission.discodeit.dto.entity.notification.NotificationDto;
import com.sprint.mission.discodeit.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public List<NotificationDto> getAllNotifications() {
        return notificationService.getAll();
    }

    @GetMapping("/{notificationId}")
    public ResponseEntity<Void> confirm(@AuthenticationPrincipal DiscodeitUserDetails userDetails,
                                        @PathVariable(value = "notificationId") UUID id) {
        notificationService.confirm(userDetails.getUserDto().id(), id);
        return ResponseEntity.noContent().build();
    }
}
