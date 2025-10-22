package com.sprint.mission.discodeit.entity.dto;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * 파일 저장용 Channel DTO
 */
public class ChannelDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    // 공통
    private UUID uuid;
    private Long createdAt;
    private Long updatedAt;

    // 엔터티 매핑 필드
    private String channelName;        // Channel.getDisplayName()
    private String type;               // ChannelType.name()
    private List<String> memberUserIds;    // 채널 멤버의 userId 목록
    private List<String> moderatorUserIds; // 채널 운영자의 userId 목록

    // 권장 생성자 (엔터티 정합성)
    public ChannelDTO(UUID uuid, Long createdAt, Long updatedAt,
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

    public UUID getUuid() { return uuid; }
    public Long getCreatedAt() { return createdAt; }
    public Long getUpdatedAt() { return updatedAt; }
    public String getChannelName() { return channelName; }
    public String getType() { return type; }
    public List<String> getMemberUserIds() { return memberUserIds; }
    public List<String> getModeratorUserIds() { return moderatorUserIds; }
}
