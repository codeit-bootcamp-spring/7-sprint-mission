package com.sprint.mission.discodeit.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@ToString
public class Message extends BaseEntity {
    private String content;
    private String userName;
    private final UUID authorId;
    private final UUID channelId;
    private final List<UUID> attachmentIds;
    private boolean isDeleted;

    @Builder
    public Message(String content, String userName, UUID authorId, UUID channelId, List<UUID> attachmentIds) {
        this.content = VerifiedUtils.verifyContent(content);
        this.userName = VerifiedUtils.verifyName(userName);
        this.authorId = VerifiedUtils.verifyNull(authorId);
        this.channelId = VerifiedUtils.verifyNull(channelId);
        this.attachmentIds = VerifiedUtils.verifyNull(attachmentIds);
        this.isDeleted = false;
    }


    public void setUserName(String name) {
        if(isDeleted) { throw new IllegalStateException("Cannot set userName on deleted Message"); }
        String vn = VerifiedUtils.verifyName(name);
        if(!vn.equals(this.userName)){
            this.userName = vn;
            reUpdatedAt();
        }
    }

    public void setContent(String content) {
        if(isDeleted){ throw new IllegalStateException("Cannot set content on deleted Message"); }
        String vn = VerifiedUtils.verifyContent(content);
        if(!vn.equals(this.content)){
            this.content = vn;
            reUpdatedAt();
        }
    }

    public void setAttachmentIds(List<UUID> attachmentIds) {
        Objects.requireNonNull(this.attachmentIds);
        this.attachmentIds.clear();
        this.attachmentIds.addAll(attachmentIds);
        reUpdatedAt();
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
}
