package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.notification.NotificationDto;
import com.sprint.mission.discodeit.exception.domain.notification.NotificationNotExistException;
import com.sprint.mission.discodeit.exception.domain.role.InvalidAccessException;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.storage.NotificationStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationStorage notificationStorage;

    @GetMapping
    public ResponseEntity<List<NotificationDto>> getNotifications(
            @AuthenticationPrincipal DiscodeitUserDetails userDetails
    ) {

        List<NotificationDto> dtoList = notificationStorage.getUserNotification(userDetails.getUserDto().id());
        return new ResponseEntity<List<NotificationDto>>(dtoList, HttpStatus.OK);
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<NotificationDto> checkNotification(@PathVariable("notificationId") UUID notificationId, @AuthenticationPrincipal DiscodeitUserDetails userDetails) {

        NotificationDto notificationDto = notificationStorage.get(notificationId);
        if(notificationDto.receiverId()!=userDetails.getUserDto().id()) throw new InvalidAccessException();
        if(notificationDto == null) throw new NotificationNotExistException();
        notificationStorage.delete(notificationId);
        return new ResponseEntity<NotificationDto>(notificationDto,HttpStatus.OK);


    }

}
