package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.util.*;

public class FileUserRepository extends AbstractFileRepository<User, UUID> implements UserRepository {

    private final String filePath;

    public FileUserRepository(String fileDirectory) {
        this.filePath = fileDirectory + File.separator + "users.ser";
    }
    @Override
    protected String getFilePath() {
        return filePath;
    }

    @Override
    protected UUID getId(User user) {
        return user.getId();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return findAll().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return findAll().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }
}
