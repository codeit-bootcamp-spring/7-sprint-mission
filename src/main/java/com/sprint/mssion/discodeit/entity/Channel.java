package com.sprint.mssion.discodeit.entity;

public class Channel {
    private final Common common;
    private ChannelType type;
    private String channelName;
    private String desc;

    public Channel(ChannelType type, String channelName, String desc) {
        this.type = type;
        this.channelName = channelName;
        this.desc = desc;
        this.common = new Common();
    }

    public Common getCommon() {
        return common;
    }

    public ChannelType getType() {
        return type;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getDesc() {
        return desc;
    }

    public void setType(ChannelType type) {
        this.type = type;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public enum ChannelType {
        PUBLIC, PRIVATE
    }

    @Override
    public String toString() {
        return "Channel{" +
                ", desc='" + desc + '\'' +
                ", channelName='" + channelName + '\'' +
                ", type=" + type +
                ", common=" + common +
                '}';
    }
}
