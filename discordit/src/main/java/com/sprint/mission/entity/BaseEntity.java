package com.sprint.mission.entity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class BaseEntity {
    protected UUID uuid;
    protected Long createdAt;
    protected Long updatedAt;

    BaseEntity () {
        this.uuid = UUID.randomUUID();
        this.createdAt = getUnixTimestamp();
        this.updatedAt = createdAt;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getCreatedAt() {
        return timeFormatter(createdAt);
    }

    public String getUpdatedAt() {
        return timeFormatter(createdAt);
    }

    public void update() {
        createdAt = Instant.now().getEpochSecond();
    }

    protected static long getUnixTimestamp() {
        return Instant.now().getEpochSecond();
    }

    private String timeFormatter(long unixTime) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(unixTime),
                ZoneId.systemDefault()
        );

        // 한국어 스타일
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm:ss");
//        System.out.println(dateTime.format(formatter));
        // 출력: 2024년 03월 15일 14:30:45
        return dateTime.format(formatter);
    }

}
