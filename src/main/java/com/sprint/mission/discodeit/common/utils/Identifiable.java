package com.sprint.mission.discodeit.common.utils;

import java.io.Serializable;

/**
 * 객체가 고유한 ID를 가지고 있음을 나타내는 인터페이스입니다.
 * 주로 제네릭 타입에서 ID를 가진 객체임을 강제하는 제약 조건으로 사용됩니다.
 *
 * @param <ID> ID의 타입 (예: UUID, Long)
 */
public interface Identifiable<ID> {

    /**
     * 객체의 고유 ID를 반환합니다.
     *
     * @return 해당 객체의 ID
     */
    ID getId();
}