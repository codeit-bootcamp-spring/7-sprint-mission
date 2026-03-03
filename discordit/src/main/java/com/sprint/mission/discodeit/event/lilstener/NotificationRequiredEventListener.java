package com.sprint.mission.discodeit.event.lilstener;

import com.sprint.mission.discodeit.common.enums.ChannelScope;
import com.sprint.mission.discodeit.common.exceptions.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.common.exceptions.user.UserNotFoundException;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.event.dto.MessageCreatedEvent;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationRequiredEventListener {

    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final ReadStatusRepository readStatusRepository;

    @TransactionalEventListener
    public void on(MessageCreatedEvent event) {
        Channel channel = channelRepository.findById(event.channelId())
                .orElseThrow(() -> new ChannelNotFoundException(event.channelId()));
        User author = userRepository.findById(event.authorId())
                .orElseThrow(() -> new UserNotFoundException(event.authorId()));
        List<User> notificationRequired = readStatusRepository.findNotificationEnabledUserByChannelId(channel.getId());
        String summary = event.content().length() > 30 ?
                event.content().substring(0, 30) + "..."
                : event.content();
        String notificationContent;
        if (channel.getType() == ChannelScope.PUBLIC) {
            notificationContent = String.format("공개 채널 %s에서 새로운 메세지가 도착했습니다. \n %s",
                    channel.getName(),
                    summary
            );
        } else {
            notificationContent = String.format("비공개 채널에서 새로운 메세지가 도착했습니다. \n %s", summary);
        }
        sendMessageToReceivers(notificationRequired, notificationContent);
    }

    private void sendMessageToReceivers(List<User> notificationRequired, String notificationContent) {

    }
}
