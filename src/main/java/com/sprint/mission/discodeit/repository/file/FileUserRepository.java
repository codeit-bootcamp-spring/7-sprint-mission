package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FileUserRepository extends FileBaseRepository<User> implements UserRepository {

    private static final String USER_DATA_FILE = "userData.ser";

    public FileUserRepository() {
        super(USER_DATA_FILE);
    }

    public Optional<User> findByEmail(String email) {
        return data.values().stream()
                .filter(user -> user.getEmail()
                        .equals(email)).findFirst();
    }

    @Override
    public Optional<User> findByUserName(String userName) {
        return data.values().stream()
                .filter(user -> user.getEmail()
                        .equals(userName)).findFirst();
    }

}
