package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exceptions.UserStatusAlreadyExistException;
import com.sprint.mission.discodeit.exceptions.UserStatusNotExistException;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
@Primary
public class FileUserStatusRepository implements UserStatusRepository {
    private static final Map<UUID, UserStatus> data = new HashMap<>();

    @Override
    public void save(UserStatus userStatus) {
        if (existsByUser(userStatus.getUser())) {
            throw new UserStatusAlreadyExistException(userStatus.getUser());
        }
        data.put(userStatus.getUuid(), userStatus);
        write();
    }

    @Override
    public void update(UserStatus userStatus) {
        if (!exists(userStatus)) {
            throw new UserStatusNotExistException(userStatus);
        }
        data.replace(userStatus.getUuid(), userStatus);
        write();
    }

    @Override
    public void deleteById(UUID id) {
        if (!existsById(id)) {
            throw new UserStatusNotExistException(id);
        }
        data.remove(id);
        write();
    }

    @Override
    public UserStatus findById(UUID id) {
        return data.get(id);
    }

    @Override
    public UserStatus findByUser(User user) {
        return data.values().stream()
                .filter(u -> u.getUser().equals(user))
                .findFirst()
                .orElseThrow(() -> new UserStatusNotExistException(user));
    }

    @Override
    public List<UserStatus> findAll() {
        return List.copyOf(data.values());
    }

    @Override
    public boolean exists(UserStatus userStatus) {
        return data.containsValue(userStatus);
    }

    @Override
    public boolean existsById(UUID id) {
        return data.containsKey(id);
    }
    @Override
    public boolean existsByUser(User user) {
        return data.values().stream()
                .anyMatch(u -> u.getUser().equals(user));
    }

    private void write() {
        DataWriter.writeUserStatus(data);
    }
}
