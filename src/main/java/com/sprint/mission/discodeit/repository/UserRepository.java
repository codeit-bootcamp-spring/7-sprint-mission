package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import java.util.UUID;

// User 전용 저장소 인터페이스
public interface UserRepository extends CrudRepository<UUID, User> {
    // 필요시 사용자 전용 조회 메서드 추가 가능 (e.g findByEmail, findByUsername 등)
}
