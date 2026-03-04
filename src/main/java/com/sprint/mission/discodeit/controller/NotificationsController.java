package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.dto_Neo.NotificationDto;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.basic.NotificationsService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationsController {
    private final NotificationsService notificationsService;

    @GetMapping
    public ResponseEntity<List<NotificationDto>> getNotifications(
        @AuthenticationPrincipal DiscodeitUserDetails userDetails) {

        log.info("💙💙💙GET /api/notifications - userDetails : {}", userDetails);

        List<NotificationDto> notificationDtoList = notificationsService.getNotifications(userDetails);

        return ResponseEntity.status(HttpStatus.OK).body(notificationDtoList);
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<?> deleteNotification(
        @PathVariable("notificationId") UUID notificationId,
        @AuthenticationPrincipal DiscodeitUserDetails userDetails) {

        log.info("💙💙💙 deleteNotification = {} /n💙💙💙userDetails = {}", notificationId, userDetails);

        notificationsService.deleteNotification(notificationId, userDetails);

        return ResponseEntity.noContent().build(); // 204 Void
    }
}
