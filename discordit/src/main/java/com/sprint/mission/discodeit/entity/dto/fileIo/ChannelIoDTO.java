package com.sprint.mission.discodeit.entity.dto.fileIo;

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
    private String type;               // ChannelType.name()
    private List<String> memberUserIds;    // 채널 멤버의 userId 목록
    private List<String> moderatorUserIds; // 채널 운영자의 userId 목록

    // 권장 생성자 (엔터티 정합성)
    public ChannelIoDTO(UUID uuid, Instant createdAt, Instant updatedAt,
                        String channelName, String type,
                        List<String> memberUserIds, List<String> moderatorUserIds) {
        this.uuid = uuid;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.channelName = channelName;
        this.type = type;
        this.memberUserIds = memberUserIds;
        this.moderatorUserIds = moderatorUserIds;
    }

}
