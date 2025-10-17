package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.File_Common;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.file.File_UserService;

import java.util.List;

public class File_UserRepository implements UserRepository {
    public File_UserRepository() {
        File_Common.fileLoading();
    }

    @Override
    public void createUser(List<User> userList, String message) {
        File_Common.fileWrite(userList, File_UserService.FILE_PATH, message);
    }

    @Override
    public void deleteUser(String name) {
    }
}
