package com.sprint.mission.discodeit.event.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.type.ChannelType;
import com.sprint.mission.discodeit.event.ChannelCreatedEvent;
import com.sprint.mission.discodeit.repository.SseEmitterRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class ChannelSseTopicListener {

    private final ObjectMapper objectMapper;
    private final ChannelService channelService;
    private final SseService sseService;
    private final SseEmitterRepository sseEmitterRepository;

    @KafkaListener(topics = "discodeit.ChannelCreatedEvent")
    public void handleChannelCreated(String kafkaEvent) {
        try {
            ChannelCreatedEvent channelCreatedEvent =
                    objectMapper.readValue(kafkaEvent, ChannelCreatedEvent.class);

            ChannelResponseDto responseDto =
                    channelService.getChannel(channelCreatedEvent.getChannelId());

            if (responseDto.type() == ChannelType.PRIVATE) {
                sseService.send(
                        responseDto.participants().stream()
                                .map(UserResponseDto::id)
                                .toList(),
                        "channels.created",
                        responseDto
                );
            } else {
                Set<UUID> connectedUserIds = sseEmitterRepository.findConnectedUserIds();
                sseService.send(
                        connectedUserIds.stream().toList(),
                        "channels.created",
                        responseDto
                );
            }

        } catch (JsonProcessingException e) {
            log.error("채널 생성 카프카 수신 오류, kafkaEvent={}", kafkaEvent, e);
            throw new RuntimeException(e);
        }
    }
}
