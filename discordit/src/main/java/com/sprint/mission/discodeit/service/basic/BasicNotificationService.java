package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.entity.notification.NotificationDto;
import com.sprint.mission.discodeit.dto.entity.notification.request.NotificationCreateRequest;
import com.sprint.mission.discodeit.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicNotificationService implements NotificationService {

    @Override
    public NotificationDto getByUserId(NotificationCreateRequest request) {
        return null; // TODO
    }
}
