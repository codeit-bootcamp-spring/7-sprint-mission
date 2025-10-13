package entity;

import java.util.UUID;

public class Message extends BaseEntity {
    private final UUID senderId;
    private final UUID receiverId;
    private final String content;

    public Message(UUID senderId, UUID receiverId, String content) {
        super();
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
    }
    public UUID getSenderId() {
        return senderId;
    }

    public UUID getReceiverId() {
        return receiverId;
    }

    public String getContent() {
        return content;
    }
}
