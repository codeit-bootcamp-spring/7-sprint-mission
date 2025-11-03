package com.sprint.mission.discodeit.dto.fileIo;

import com.sprint.mission.discodeit.enums.ChannelScope;
import com.sprint.mission.discodeit.enums.ChannelType;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * 파일 저장용 Channel DTO
 */
@Getter
public class ChannelIoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    // 공통
    private UUID uuid;
    private Instant createdAt;
    private Instant updatedAt;

    // 엔터티 매핑 필드
    private String channelName;        // Channel.getDisplayName()
    private ChannelType type;               // ChannelType.name()
    private ChannelScope scope;
    private String description;
    private List<UUID> memberUuids;    // 채널 멤버의 UUID 목록
    private List<UUID> moderatorUuids; // 채널 운영자의 UUID 목록

    public ChannelIoDTO(UUID uuid, Instant createdAt, Instant updatedAt,
                        String channelName, ChannelType type, ChannelScope scope, String description,
                        List<UUID> memberUuids, List<UUID> moderatorUuids) {
        this.uuid = uuid;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.channelName = channelName;
        this.type = type;
        this.scope = scope;
        this.description = description;
        this.memberUuids = memberUuids;
        this.moderatorUuids = moderatorUuids;
    }

}
