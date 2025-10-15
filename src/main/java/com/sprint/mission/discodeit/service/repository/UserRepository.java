package com.sprint.mission.discodeit.service.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.Optional;

public interface UserRepository extends BaseRepository<User> {
    public Optional<User> findByEmail(String email);
}
