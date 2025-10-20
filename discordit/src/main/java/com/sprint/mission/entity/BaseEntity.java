package com.sprint.mission.entity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
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

    public Long getCreatedAt() {
        return createdAt;
    }

    // 더미 데이터 초기 생성용
    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public String getFormattedCreatedAt() {
        return timeFormatter(createdAt);
    }

    public String getCreatedTime() {
        LocalDateTime dateTime = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(createdAt),
                ZoneId.systemDefault()
        );
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return dateTime.format(formatter);
    }

    public String getSimpleCreatedDate() {
        LocalDateTime dateTime = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(createdAt),
                ZoneId.systemDefault()
        );
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd");
        return dateTime.format(formatter);
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public String getFormattedUpdatedAt() {
        return timeFormatter(updatedAt);
    }

    public void update() {
        updatedAt = Instant.now().getEpochSecond();
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
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
