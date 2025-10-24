package com.sprint.mission.discodeit.common.utils;

/**
 * 객체가 논리적 삭제(Soft Delete)를 지원함을 나타내는 인터페이스입니다.
 */
public interface Deletable {

    /**
     * 객체를 논리적 삭제 상태로 변경합니다.
     */
    void softDelete();

    /**
     * 객체가 논리적으로 삭제된 상태인지 확인합니다.
     *
     * @return 삭제된 상태이면 true, 그렇지 않으면 false
     */
    boolean isDeleted();
}