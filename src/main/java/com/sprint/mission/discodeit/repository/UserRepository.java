package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends BaseRepository<User>{

    // 이메일(아이디) 중복 확인용
    Optional<User> findByEmail(String email);

    // 닉네임 중복 확인용
    Optional<User> findByUserName(String userName);
}
