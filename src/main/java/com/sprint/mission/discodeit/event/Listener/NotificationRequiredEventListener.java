package com.sprint.mission.discodeit.event.Listener;

import com.sprint.mission.discodeit.dto.request.notification.NotificationDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.event.MessageCreatedEvent;
import com.sprint.mission.discodeit.event.RoleUpdatedEvent;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.storage.NotificationStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationRequiredEventListener {

    private final NotificationStorage  notificationStorage;
    private final ReadStatusRepository  readStatusRepository;
    private final ChannelRepository channelRepository;

@TransactionalEventListener
@Async("notificationExecutor")
@Transactional(propagation = Propagation.REQUIRES_NEW)
    public void on(MessageCreatedEvent event) {
    Channel channel = channelRepository.findById(event.getChannelId()).orElseThrow();
    List<ReadStatus> readStatuses = readStatusRepository.findByChannel(channel);
    Stream<User> userStream = readStatuses.stream().filter(
            x->{

                return !x.getUser().getId().equals(event.getAuthorId());
            }
    )
            .filter(ReadStatus::isNotificationEnabled)
            .map(ReadStatus::getUser);

    String content = String.format("채널 id:  %s, 유저 id: %s , 메세지: %s ",
            event.getChannelId().toString(), event.getAuthorId().toString(), event.getContent());

    userStream.forEach(
            user->{
                log.info("유저 이름 : {}", user.getUserName());
                NotificationDto notificationDto = new NotificationDto(
                        UUID.randomUUID(),
                        Instant.now(),
                        user.getId(),
                        "메세지 알림",
                        content
                );
                notificationStorage.save(notificationDto);
            }
    );

}

@TransactionalEventListener
@Async("notificationExecutor")
    public void on(RoleUpdatedEvent event) {
    String content = String.format("권한이 변경되었습니다. : %s",event.getRole().name());
    NotificationDto notificationDto = new NotificationDto(
            UUID.randomUUID(),
            Instant.now(),
            event.getUserId(),
            "유저 권한 변경 알림",
            content
    );
    notificationStorage.save(notificationDto);

}

}
