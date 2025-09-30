package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.Utils.Deletable;
import com.sprint.mission.discodeit.Utils.Identifiable;

import java.util.UUID;

/**
 * 모든 엔티티가 공통적으로 가져야 할 기본 속성과 기능을 정의하는 추상 클래스입니다.
 * - 고유 ID (id)
 * - 생성 시각 (createdAt)
 * - 최종 수정 시각 (updatedAt)
 * - 논리적 삭제 상태 (isDeleted)
 * <p>
 * `abstract` 키워드를 사용해 이 클래스 자체로는 객체를 만들 수 없으며, 반드시 다른 엔티티가 상속받아 사용해야 합니다.
 */
public abstract class BaseEntity implements Identifiable<UUID>, Deletable {

    /**
     * 모든 엔티티를 고유하게 식별하는 ID입니다.
     * `final`로 선언하여 한 번 할당된 후에는 절대 변경되지 않도록 보장합니다.
     */
    private final UUID id;

    /**
     * 엔티티가 처음 생성된 시각을 저장합니다. (Unix Timestamp)
     * `final`로 선언하여 변경되지 않도록 보장합니다.
     */
    private final Long createdAt;

    /**
     * 엔티티가 마지막으로 수정된 시각을 저장합니다. (Unix Timestamp)
     */
    private Long updatedAt;

    /**
     * 논리적 삭제(Soft Delete) 상태를 나타내는 플래그입니다.
     * true이면 삭제된 것으로 간주합니다.
     */
    private boolean isDeleted = false;


    /**
     * BaseEntity의 생성자입니다.
     * 이 클래스를 상속받는 자식 클래스가 생성될 때 `super()`를 통해 호출됩니다.
     * 객체가 생성되는 시점에 ID와 타임스탬프를 초기화하는 역할을 합니다.
     */
    protected BaseEntity() {
        this.id = UUID.randomUUID();
        long now = System.currentTimeMillis();
        this.createdAt = now;
        this.updatedAt = now;
    }

    /**
     * {@inheritDoc}
     * Deletable 인터페이스의 구현부입니다.
     * isDeleted 플래그를 true로 설정하고, 수정 시각을 갱신합니다.
     */
    @Override
    public void softDelete() {
        this.isDeleted = true;
        updateTimestamp();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDeleted() {
        return isDeleted;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID getId() {
        return id;
    }

    /**
     * 생성 시각을 반환합니다.
     *
     * @return Long 타입의 Unix Timestamp
     */
    public Long getCreatedAt() {
        return createdAt;
    }

    /**
     * 최종 수정 시각을 반환합니다.
     *
     * @return Long 타입의 Unix Timestamp
     */
    public Long getUpdatedAt() {
        return updatedAt;
    }

    /**
     * 엔티티의 내용이 변경되었을 때, 최종 수정 시각을 현재 시간으로 갱신하는 메서드입니다.
     * 자식 클래스의 수정 관련 메서드 내부에서 호출됩니다.
     */
    protected void updateTimestamp()  {
//        try {
//            Thread.sleep(30);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }  수정사항이 바로 반영 되면 이전 반영 시간과 같아져 테스트를 위해 넣었던 코드입니다.
        this.updatedAt = System.currentTimeMillis();
    }
}