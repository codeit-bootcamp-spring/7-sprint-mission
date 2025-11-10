package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
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

    private Instant recentPostTime;
    private String name;
    private String description;
    private boolean isTextChannel;
    private boolean isPublic;
    @Builder.Default
    private HashSet<UUID> userIdList  = new HashSet<>();

    public static ChannelReadResponseDto from(Channel channel, ReadStatus readStatus) {
        return ChannelReadResponseDto.builder()
                .name(channel.getName())
                .description(channel.getDescription())
                .isTextChannel(channel.isTextChannel())
                .isPublic(channel.isPublic())
                .recentPostTime(readStatus.getReadLastTime())
                .build();
    }

}
