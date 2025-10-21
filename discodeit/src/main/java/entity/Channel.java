package entity;

import java.util.UUID;

public class Channel extends BaseEntity {
    private final String name;
    private String topic;
    private final UUID ownerId;
    private boolean isPrivate;

    public Channel(String name, UUID ownerId, boolean isPrivate) {
        super();
        this.name = name;
        this.topic = topic;
        this.ownerId = ownerId;
        this.isPrivate = isPrivate;
    }

    public String getName() {
        return name;
    }

    public String getTopic() {
        return topic;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void changeTopic(String newTopic) {
        this.topic = newTopic;
        touch();
    }
}
