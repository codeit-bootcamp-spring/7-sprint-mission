package com.sprint.mission.discodeit.event.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.event.user.UserCreatedEvent;
import com.sprint.mission.discodeit.event.user.UserDeletedEvent;
import com.sprint.mission.discodeit.event.user.UserUpdatedEvent;
import com.sprint.mission.discodeit.service.SseService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserSseTopicListener {

    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final SseService sseService;

    @KafkaListener(topics = "discodeit.UserCreatedEvent")
    public void handleUserCreated(String kafkaEvent) {
        try {
            UserCreatedEvent event =
                    objectMapper.readValue(kafkaEvent, UserCreatedEvent.class);

            UserResponseDto responseDto =
                    userService.getUserById(event.getUserId());

            sendUserEvent("users.created", responseDto);

        } catch (Exception e) {
            log.error("사용자 생성 이벤트 처리 오류, kafkaEvent={}", kafkaEvent, e);
            throw new RuntimeException(e);
        }
    }


    @KafkaListener(topics = "discodeit.UserUpdatedEvent")
    public void handleUserUpdated(String kafkaEvent) {
        try {
            UserUpdatedEvent event =
                    objectMapper.readValue(kafkaEvent, UserUpdatedEvent.class);

            UserResponseDto responseDto = userService.getUserById(event.getUserId());

            sendUserEvent("users.updated", responseDto);
        } catch (Exception e) {
            log.error("사용자 업데이트 이벤트 처리 오류, kafkaEvent={}", kafkaEvent, e);
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "discodeit.UserDeletedEvent")
    public void handleUserDeleted(String kafkaEvent) {
        try {
            UserDeletedEvent event =
                    objectMapper.readValue(kafkaEvent, UserDeletedEvent.class);

            UserResponseDto responseDto = event.getUserResponseDto();

            sendUserEvent("users.deleted", responseDto);

        } catch (Exception e) {
            log.error("사용자 삭제 이벤트 처리 오류, kafkaEvent={}", kafkaEvent, e);
            throw new RuntimeException(e);
        }
    }

    private void sendUserEvent(String eventName, UserResponseDto responseDto) {
        sseService.send(
                List.of(responseDto.id()),
                eventName,
                responseDto
        );
    }
}
