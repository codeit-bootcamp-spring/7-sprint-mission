package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;


import java.io.*;
import java.util.*;

public class FileUserRepository extends AbstractFileRepository<User, UUID>{

    private static final String USER_FILE_PATH = "users.ser";

    @Override
    protected String getFilePath() {
        return USER_FILE_PATH;
    }

    @Override
    protected UUID getId(User user) {
        return user.getId();
    }
}
