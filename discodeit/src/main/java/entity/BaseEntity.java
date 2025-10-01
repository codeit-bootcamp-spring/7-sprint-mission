package entity;
import java.util.UUID;
public abstract class BaseEntity {
    private final UUID id;
    private final long createdAt;
    private long updatedAt;

    public BaseEntity() {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
    }
}
