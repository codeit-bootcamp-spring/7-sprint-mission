package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import lombok.Getter;

@Getter
public class Channel implements Serializable {

    private static final long serialVersionUID = 1L;

    public List<UUID> getParticipantUserIds() {
        return List.of();
    }

    // 서비스에서 사용하는 가시성(Enum)
    public enum Visibility { PUBLIC, PRIVATE }

    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;

    private Visibility visibility;   // ✅ 서비스에서 참조
    private ChannelType type;        // (기존 호환용: enum 값이 있다면 PUBLIC/PRIVATE로 맞춰주세요)
    private String name;
    private String description;

    // ---- 생성자는 외부에서 직접 호출하지 않게 숨깁니다 ----
    private Channel(Visibility visibility, ChannelType type, String name, String description) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;     // ✅ 최초 생성 시점으로 세팅
        this.visibility = visibility;
        this.type = type;
        this.name = name;
        this.description = description;
    }

    // ---- 팩토리 메서드 ----
    /** PUBLIC 채널 생성 (name/description 사용) */
    public static Channel createPublic(String name, String description) {
        // ChannelType.PUBLIC 이 없다면 null 로 두셔도 됩니다.
        return new Channel(Visibility.PUBLIC, ChannelType.PUBLIC, name, description);
    }

    /** PRIVATE 채널 생성 (요구사항상 name/description 없이) */
    public static Channel createPrivate(List<UUID> uuids) {
        // ChannelType.PRIVATE 이 없다면 null 로 두셔도 됩니다.
        return new Channel(Visibility.PRIVATE, ChannelType.PRIVATE, null, null);
    }

    // ---- 수정 메서드 (PRIVATE 수정 금지는 서비스 레이어에서 검사) ----
    public void update(String newName, String newDescription) {
        boolean anyValueUpdated = false;

        if (newName != null && !newName.equals(this.name)) {
            this.name = newName;
            anyValueUpdated = true;
        }
        if (newDescription != null && !newDescription.equals(this.description)) {
            this.description = newDescription;
            anyValueUpdated = true;
        }

        if (anyValueUpdated) {
            this.updatedAt = Instant.now();
        }
    }
}
