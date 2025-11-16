package com.sprint.mission.discodeit.entity.dto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
public record ChannelDto( //all private final
//       //@NotBlank(message = "channelId is mandatory")
                          UUID id,
                          Instant createdAt,
                          Instant updatedAt,

                          String channelName,
                          String description,
                          ChannelType channelType

//                          // for update
//                          String newName,
//                          String newDescription

//        List<UUID> participantIds; // PRIVATE 일 경우 사용
//        private Instant lastMessageAt; // [ ] 해당 채널의 가장 최근 메시지의 시간 정보를 포함합니다.
) {
//    @Builder
    public static ChannelDto create(Channel channel) {
        return ChannelDto.builder()
            .id(channel.getId())
            .createdAt(channel.getCreatedAt())
            .updatedAt(channel.getUpdatedAt())
            .channelName(channel.getChannelName())
            .description(channel.getDescription())
            .channelType(channel.getChannelType())
            .build();
    }
//    public static ChannelDto update(String newName, String newDescription){
//        return ChannelDto.builder()
//            .newName(newName)
//            .newDescription(newDescription)
//            .build();
//    }
}
