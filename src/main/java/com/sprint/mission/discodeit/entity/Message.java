package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message extends BaseEntity {
    private String content;
    private String userName;
    private final UUID authorId;
    private final UUID channelId;
    private boolean isDeleted;

    public Message(String content, String userName, UUID authorId, UUID channelId) {
        this.content = VerifiedUtils.verifyContent(content);
        this.userName = VerifiedUtils.verifyName(userName);
        this.authorId = VerifiedUtils.verifyNull(authorId);
        this.channelId = VerifiedUtils.verifyNull(channelId);
        this.isDeleted = false;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String name) {
        if(isDeleted) { throw new IllegalStateException("Cannot set userName on deleted Message"); }
        String vn = VerifiedUtils.verifyName(name);
        if(!vn.equals(this.userName)){
            this.userName = vn;
            reUpdatedAt();
        }
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        if(isDeleted){ throw new IllegalStateException("Cannot set content on deleted Message"); }
        String vn = VerifiedUtils.verifyContent(content);
        if(!vn.equals(this.content)){
            this.content = vn;
            reUpdatedAt();
        }
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public UUID getChannelId() {
        return channelId;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public boolean delete() {
        if(!isDeleted){
            isDeleted = true;
            reUpdatedAt();
            return true;
        }
        return false;
    }

    public String getDisplayText() {
        if(isDeleted){
            return "Deleted Message";
        } else {
            return content;
        }
    }

    @Override
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                '}';
    }
}
