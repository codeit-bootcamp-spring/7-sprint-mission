package com.sprint.mission.discodeit.entity;

public class Channel extends BaseEntity{

    public String channelName;
    private final ChannelType type;    // 채널타입

    public enum ChannelType {
        TEXT("텍스트"), VOICE("음성"),
        FORUM("포럼");

        private final String descType;

        ChannelType(String descType) {
            this.descType = descType;
        }

        public String getDescType() {
            return descType;
        }

    }

    public Channel(String channelName, ChannelType type) {
        super();
        this.channelName = channelName;
        this.type = type;
    }



    public ChannelType getType() {
        return type;
    }
}
