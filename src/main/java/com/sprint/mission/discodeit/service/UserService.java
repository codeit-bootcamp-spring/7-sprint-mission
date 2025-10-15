package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import java.util.*;

/**
 * ✅ UserService 인터페이스
 * - User 도메인에 대한 CRUD 규칙을 정의한다.
 * - 구현체(JCFUserService)에서 이 규칙을 구체적으로 실현한다.
 */
public interface UserService {

    // C: 사용자 생성 (이름, 이메일 필수)
    User create(String username, String email);

    // R: 단건 조회
    User read(UUID id);

    // R: 전체 조회
    List<User> readAll();

    // U: 이름 수정
    User updateUsername(UUID id, String newName);

    // U: 이메일 수정
    User updateEmail(UUID id, String newEmail);

    // D: 삭제
    boolean delete(UUID id);

    // 추가: 활성/비활성 전환
    User activate(UUID id);
    User deactivate(UUID id);
}