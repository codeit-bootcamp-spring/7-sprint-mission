package com.sprint.mssion.discodeit.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel {
    private final Common common;
    private ChannelType type;
    private String channelName;
    private String desc;
    private final List<UUID> joiner;
    private final List<UUID> messages;

    public Channel(ChannelType type, String channelName, String desc) {
        this.type = type;
        this.channelName = channelName;
        this.desc = desc;
        this.common = new Common();
        this.joiner = new ArrayList<>();
        this.messages = new ArrayList<>();
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

    public List<UUID> getJoiner() {
        return joiner;
    }

    public List<UUID> getMessages() {
        return messages;
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

    public void addJoiner(UUID userId){
        if(!joiner.contains(userId)){
            joiner.add(userId);
        }
    }

    public void addMessages(UUID messageId){
        if(!messages.contains(messageId)){
            messages.add(messageId);
        }
    }

    public void removeJoiner(UUID userId){
        joiner.remove(userId);
    }

    public void  removeMessages(UUID messageId){
        messages.remove(messageId);
    }

    @Override
    public String toString() {
        return "Channel{" +
                "messages=" + messages +
                ", joiner=" + joiner +
                ", desc='" + desc + '\'' +
                ", channelName='" + channelName + '\'' +
                ", type=" + type +
                ", common=" + common +
                '}';
    }
}
