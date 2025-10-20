package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

public class FileUserRepository extends FileBaseRepository<User> implements UserRepository {

    public FileUserRepository(String rootPath) {
        super(rootPath + "Data.ser");
    }

    public Optional<User> findByEmail(String email) {
        return data.values().stream()
                .filter(user -> user.getEmail()
                        .equals(email)).findFirst();
    }

}
