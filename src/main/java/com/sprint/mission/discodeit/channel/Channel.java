package com.sprint.mission.discodeit.channel;

import com.sprint.mission.discodeit.common.entity.BaseEntity;
import com.sprint.mission.discodeit.config.enums.ChannelType;

import java.util.UUID;

/**
 * 채널의 정보를 담는 핵심 도메인 엔티티입니다.
 * 채널의 생성과 상태 변경에 대한 비즈니스 로직을 포함합니다.
 */
public class Channel extends BaseEntity<UUID> {

    /**
     * 채널의 이름 (사용자에게 표시됨, 고유해야 함)
     */
    private String channelName;
    /**
     * 채널의 타입 (예: DIRECT, GROUP)
     */
    private ChannelType channelType;
    /**
     * 채널의 주제 또는 설명
     */
    private String topic;
    /**
     * 채널의 공개 여부 (true: 비공개, false: 공개)
     */
    private boolean isPrivate = true;

    /**
     * 외부에서의 직접적인 생성을 막고, static create 메서드를 통하도록 강제합니다.
     */
    private Channel() {
        super(UUID.randomUUID());
    }

    /**
     * Channel 객체를 안전하게 생성하는 정적 팩토리 메서드입니다.
     *
     * @param channelName 채널 이름 (필수)
     * @param channelType 채널 타입 (null일 경우 기본값 CHAT으로 설정)
     * @param topic       채널 주제
     * @param isPrivate   공개 여부
     * @return 완전히 생성된 Channel 객체
     */
    public static Channel create(String channelName, ChannelType channelType, String topic, boolean isPrivate) {
        if (channelName == null || channelName.isBlank()) {
            throw new IllegalArgumentException("채널 이름은 필수입니다.");
        }
        // 채널 타입이 지정되지 않은 경우, 기본 타입을 설정해주는 방어 로직
        if (channelType == null) {
            channelType = ChannelType.CHAT; // CHAT을 기본값으로 가정
        }
        Channel channel = new Channel();
        channel.channelName = channelName;
        channel.channelType = channelType;
        channel.topic = topic;
        channel.isPrivate = isPrivate;
        return channel;
    }

    /**
     * 채널의 설정을 변경합니다.
     * 변경을 원하는 필드의 값만 전달하고, 원하지 않는 필드는 null로 남겨둡니다.
     *
     * @param channelName 변경할 채널 이름
     * @param channelType 변경할 채널 타입
     * @param topic       변경할 채널 주제
     * @param isPrivate   변경할 공개 여부
     */
    public void changeSettings(String channelName, ChannelType channelType, String topic, Boolean isPrivate) {
        boolean isChanged = false;

        if (channelName != null && !channelName.isBlank()) {
            this.channelName = channelName;
            isChanged = true;
        }
        if (channelType != null) {
            this.channelType = channelType;
            isChanged = true;
        }
        if (topic != null) {
            this.topic = topic;
            isChanged = true;
        }
        if (isPrivate != null) {
            this.isPrivate = isPrivate;
            isChanged = true;
        }

        // 실제 필드 값이 하나라도 변경되었을 때만 수정 시각을 갱신합니다.
        if (isChanged) {
            super.updateTimestamp();
        }
    }

    // --- Getters ---

    public String getChannelName() {
        return channelName;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public ChannelType getChannelType() {
        return channelType;
    }

    public String getTopic() {
        return topic;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id=" + getId() +
                ", channelName='" + channelName + '\'' +
                ", channelType=" + channelType +
                ", topic='" + topic + '\'' +
                ", isPrivate=" + isPrivate +
                '}';
    }
}