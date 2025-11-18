package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entityElement.ChannelType;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChannelReadResponseDto {
    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    private String name;
    private String description;
    private ChannelType type;

    private boolean isTextChannel;
    private HashSet<UUID> participantIds ;

    public static ChannelReadResponseDto from(Channel channel) {
        return ChannelReadResponseDto.builder()
                .name(channel.getName())
                .description(channel.getDescription())
                .isTextChannel(channel.isTextChannel())
                .type(channel.isPublic() ? ChannelType.PUBLIC : ChannelType.PRIVATE)
                .id(channel.getId())
                .createdAt(channel.getCreatedAt())
                .updatedAt(channel.getUpdatedAt())
                .participantIds(channel.getJoinUserList()==null? new HashSet<>():channel.getJoinUserList())
                .build();
    }

}
