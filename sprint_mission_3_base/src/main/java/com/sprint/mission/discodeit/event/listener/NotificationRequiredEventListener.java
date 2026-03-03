package com.sprint.mission.discodeit.event.listener;

import com.sprint.mission.discodeit.event.MessageCreatedEvent;
import com.sprint.mission.discodeit.event.RoleUpdatedEvent;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import java.util.UUID;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class NotificationRequiredEventListener {

    private final ReadStatusRepository readStatusRepository;
    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    public NotificationRequiredEventListener(
            ReadStatusRepository readStatusRepository,
            NotificationService notificationService,
            UserRepository userRepository,
            ChannelRepository channelRepository
    ) {
        this.readStatusRepository = readStatusRepository;
        this.notificationService = notificationService;
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
    }

    @Async
    @TransactionalEventListener
    public void on(MessageCreatedEvent event) {
        var sender = userRepository.findById(event.senderId()).orElse(null);
        var channel = channelRepository.findById(event.channelId()).orElse(null);

        String senderName = sender != null ? sender.getUsername() : "Unknown";
        String channelName = channel != null ? channel.getName() : "Unknown";

        String title = senderName + " (#" + channelName + ")";
        String content = event.content() == null ? "" : event.content();

        var targets = readStatusRepository.findAllByChannelIdAndNotificationEnabledTrue(event.channelId());

        for (var rs : targets) {
            UUID receiverId = rs.getUser().getId();
            if (receiverId.equals(event.senderId())) {
                continue;
            }
            notificationService.create(rs.getUser(), title, content);
        }
    }

    @Async
    @TransactionalEventListener
    public void on(RoleUpdatedEvent event) {
        var user = userRepository.findById(event.targetUserId()).orElse(null);
        if (user == null) {
            return;
        }
        String title = "권한이 변경되었습니다.";
        String content = event.oldRole() + " -> " + event.newRole();
        notificationService.create(user, title, content);
    }
}