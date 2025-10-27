package com.sprint.mission.discodeit.entity.status;

import com.sprint.mission.discodeit.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserStatusRepository extends BaseRepository<UserStatus> {

    // 유저 아이디로 상태찾기
    Optional<UserStatus> findStatusByUserId(UUID userId);

}
