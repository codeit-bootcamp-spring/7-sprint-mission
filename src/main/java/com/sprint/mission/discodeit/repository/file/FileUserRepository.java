package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileUserRepository extends AbstractFileMapRepository<UUID, User> implements UserRepository {
    /**
     * 생성자: 파일 경로를 받아서, 기존 데이터가 있으면 로드함
     *
     * @param path
     */
    protected FileUserRepository(Path path) {
        super(path);
    }

    @Override
    public User save(User entity) {
        data.put(entity.getId(), entity);
        persist();
        return entity;
    }

    @Override
    public User findById(UUID uuid) {
        return data.get(uuid);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public boolean deleteById(UUID uuid) {
        boolean removed = data.remove(uuid) != null;
        if(removed) persist();
        return removed;
    }
}
