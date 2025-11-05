package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

@Getter
public class BaseEntity {
    protected UUID uuid;
    protected Instant createdAt;
    protected Instant updatedAt;

    BaseEntity () {
        this.uuid = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = createdAt;
    }

    public String getFormattedCreationTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm")
                .withZone(ZoneId.systemDefault());
        return formatter.format(createdAt);
    }

    protected void update() {
        updatedAt = Instant.now();
    }

    protected static long getUnixTimestamp() {
        return Instant.now().getEpochSecond();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uuid);
    }


}
