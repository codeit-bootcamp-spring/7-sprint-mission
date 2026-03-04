package com.sprint.mission.discodeit.event.lilstener;

import com.sprint.mission.discodeit.common.enums.ChannelScope;
import com.sprint.mission.discodeit.common.exceptions.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.common.exceptions.user.UserNotFoundException;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.event.dto.MessageCreatedEvent;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationRequiredEventListener {

    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final ReadStatusRepository readStatusRepository;
    private final NotificationRepository notificationRepository;

    @TransactionalEventListener
    public void on(MessageCreatedEvent event) {
        Channel channel = channelRepository.findById(event.channelId())
                .orElseThrow(() -> new ChannelNotFoundException(event.channelId()));
        User author = userRepository.findById(event.authorId())
                .orElseThrow(() -> new UserNotFoundException(event.authorId()));
        List<User> notificationRequired = readStatusRepository.findNotificationEnabledUserByChannelId(channel.getId());
        notificationRequired.remove(author);
        String title = channel.getType() == ChannelScope.PUBLIC
                ? String.format("%s (#%s)", author.getUsername(), channel.getName())
                : author.getUsername();
        String summary = event.content().length() > 30 ?
                event.content().substring(0, 30) + "..."
                : event.content();
        List<Notification> notis = notificationRequired.stream()
                .map(u -> new Notification(u, title, summary))
                .toList();
        notificationRepository.saveAll(notis);
    }
}
