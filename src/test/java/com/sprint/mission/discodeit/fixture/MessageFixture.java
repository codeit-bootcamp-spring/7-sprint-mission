package com.sprint.mission.discodeit.fixture;

import com.sprint.mission.discodeit.dto.messageDto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.messageDto.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

public class MessageFixture {

    // Request ========================================================
    public static MessageCreateRequest getMessageRequest(int index) {
        UUID userId = UUID.randomUUID();
        UUID channelId = UUID.randomUUID();

        return MessageCreateRequest.builder()
                .content("test " + index)
                .authorId(userId)
                .channelId(channelId)
                .build();
    }

    // Entity ========================================================
    public static Message getMessage(int index) {
        return toEntity(getMessageRequest(index));
    }

    private static Message toEntity(MessageCreateRequest request) {
        Message message =  Message.builder()
                .content(request.getContent())
                .author(UserFixture.getUser(1))
                .channel(ChannelFixture.getPublicChannel(1))
                .build();
        ReflectionTestUtils.setField(message, "id", UUID.randomUUID());
        return message;
    }

    // Response ========================================================
    public static MessageDto getMessageDto(int index) {
        return toDto(getMessage(index));
    }

    private static MessageDto toDto(Message message) {
        return MessageDto.builder()
                .id(message.getId())
                .content(message.getContent())
                .channelId(message.getChannel().getId())
                .author(UserFixture.getUserDto(1))
                .build();
    }
}
