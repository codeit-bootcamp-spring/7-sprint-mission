package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.dto.Dto_Message;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class Message extends BaseModel {
    private UUID channelId;
    private UUID authorId;
    private String message;
    private List<UUID> attachemntIds;

    public Message(Dto_Message dtoMessage, List<UUID> attachemntIds) {
        super();
        this.channelId = dtoMessage.channelId();
        this.authorId = dtoMessage.authorid();
        this.message = dtoMessage.message();
        this.attachemntIds = attachemntIds;
    }

    @Override
    public String toString() {
        return "message {" +
                super.toString() +
                "\n message = [" + message + "] }";
    }

    public void updateMessage( String message) {
        this.message = message;
        super.setUpdatedAt();
    }

}
