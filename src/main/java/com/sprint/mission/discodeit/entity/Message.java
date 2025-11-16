package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.dto.MessageCreateRequest;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class Message extends BaseModel {
    private UUID channelId;
    private UUID authorId;
    private String message;
    private List<UUID> attachemntIds;

    public Message(MessageCreateRequest dtoMessage, List<UUID> attachemntIds) {
        super();
        this.channelId = dtoMessage.channelId();
        this.authorId = dtoMessage.authorId();
        this.message = dtoMessage.content();
        this.attachemntIds = attachemntIds;
    }

    @Override
    public String toString() {
        return "content {" +
                super.toString() +
                "\n content = [" + message + "] }";
    }

    public void updateMessage( String message) {
        this.message = message;
        super.setUpdatedAtNow();
    }

}
