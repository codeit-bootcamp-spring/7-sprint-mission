package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class Channel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Channel channel = (Channel) o;
        return Objects.equals(common, channel.common) && type == channel.type && Objects.equals(channelName, channel.channelName) && Objects.equals(desc, channel.desc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(common, type, channelName, desc);
    }
}
