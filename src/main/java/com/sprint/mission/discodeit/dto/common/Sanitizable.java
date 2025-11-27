package com.sprint.mission.discodeit.dto.common;

// 로그용 복사본을 생성할 수 있는 DTO 마커 인터페이스
public interface Sanitizable<T> {

  T toLoggingDTO();
}
