package com.sprint.mission.discodeit.common.utils;


import java.util.Objects;
import java.util.UUID;

/**
 * ChannelUser의 복합 유니크 키를 표현하는 불변(Immutable) 데이터 클래스입니다.
 * Set이나 Map의 키로 사용하기 위해 equals()와 hashCode()를 반드시 재정의해야 합니다.
 */
public final class ParticipationRecForCls {
    /**
     * final로 선언하여 한 번 값이 할당된 후에는 변경될 수 없도록 합니다.
     */
    private final UUID channelId;
    private final UUID userId;

    /**
     * 생성자를 통해 필드 값을 초기화합니다.
     *
     * @param channelId 채널 ID
     * @param userId    사용자 ID
     */
    public ParticipationRecForCls(UUID channelId, UUID userId) {
        // null 체크를 통해 객체의 불변성과 안정성을 보장합니다.
        this.channelId = Objects.requireNonNull(channelId, "channelId는 null일 수 없습니다.");
        this.userId = Objects.requireNonNull(userId, "userId는 null일 수 없습니다.");
    }

    // --- Getters ---

    public UUID getChannelId() {
        return channelId;
    }



    public UUID getUserId() {
        return userId;
    }

    /**
     * 두 ChannelUserKey 객체가 논리적으로 같은지 비교합니다.
     * channelId와 userId가 모두 같아야 true를 반환합니다.
     */
    @Override
    public boolean equals(Object o) {
        // 1. 메모리 주소가 같으면 당연히 같은 객체
        if (this == o) return true;
        // 2. null이거나 클래스 타입이 다르면 다른 객체
        if (o == null || getClass() != o.getClass()) return false;
        // 3. 필드 값을 비교하여 같은지 확인
        ParticipationRecForCls that = (ParticipationRecForCls) o;
        return channelId.equals(that.channelId) && userId.equals(that.userId);
    }

    /**
     * equals()가 true를 반환하는 두 객체는 반드시 같은 hashCode() 값을 반환해야 합니다.
     * Set이나 Map이 객체를 효율적으로 찾기 위해 사용됩니다.
     */
    @Override
    public int hashCode() {
        // 두 필드를 조합하여 고유한 해시코드를 생성합니다.
        return Objects.hash(channelId, userId);
    }

    /**
     * 객체의 상태를 쉽게 확인하기 위한 toString() 메서드입니다.
     */
    @Override
    public String toString() {
        return "ChannelUserKey{" +
                "channelId=" + channelId +
                ", authorId=" + userId +
                '}';
    }
}
