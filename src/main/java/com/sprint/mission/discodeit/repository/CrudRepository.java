package com.sprint.mission.discodeit.repository;

import java.util.*;
import java.util.UUID;

// 공통 CRUD 규약 : 저장 로직 전담 (비즈니스 규칙 X)
public interface CrudRepository<ID, T> {
    T save (T entity);  // 생성/수정 시 저장
    T findById (ID id); // ID 단건 조회 (없으면 null 반환)
    List<T> findAll(); // 전체 조회
    boolean deleteById (ID id); // 삭제

}
