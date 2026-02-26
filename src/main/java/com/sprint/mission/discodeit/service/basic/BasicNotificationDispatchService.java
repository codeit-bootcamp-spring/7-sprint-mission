package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.event.MessageCreatedEvent;
import com.sprint.mission.discodeit.common.event.RoleUpdatedEvent;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.NotificationDispatchService;
import com.sprint.mission.discodeit.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BasicNotificationDispatchService implements NotificationDispatchService {
    private final NotificationService notificationService;
    private final ReadStatusRepository readStatusRepository;

    @Override
    public void dispatchMessageCreated(MessageCreatedEvent event) {
        List<ReadStatus> readStatuses =
                readStatusRepository.findAllByChannelIdAndNotificationEnabledTrue(event.channelId());

        String channelName = event.channelName() == null || event.channelName().isBlank()
                ? "private" : event.channelName();

        String title = event.authorName() + " (#" + channelName + ")";
        String content = event.content();

        for (ReadStatus rs : readStatuses) {
            UUID receiverId = rs.getUser().getId();

            if (receiverId.equals(event.authorId())) {
                continue;
            }

            try {
                notificationService.create(receiverId, title, content);
            } catch (Exception e) {
                log.error("Message notification create failed. message = {}, receiverId = {}",
                        event.messageId(), receiverId, e);
            }
        }
    }

    @Override
    public void dispatchRoleUpdated(RoleUpdatedEvent event) {
        String title = "권한이 변경되었습니다.";
        String content = event.oldRole() + " -> " + event.newRole();

        try {
            notificationService.create(event.userId(), title, content);
        } catch (Exception e) {
            log.error("Role notification create failed. userId = {}",
                    event.userId(), e);
        }
    }
}
