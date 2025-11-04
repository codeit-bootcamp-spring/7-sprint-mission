package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.entity.User;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.file.FileIo;
import com.sprint.mission.discodeit.service.file.Path;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "file",
        matchIfMissing = true
)
public class FileUserRepository implements UserRepository {

    private final String filename = "users";

    //로드 세이브
    //근데 잘해보면 한번에 할수있겟는데?
    @Override
    public User save(User user) {
       FileIo.save(filename, user);
      return user;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return FileIo.read(filename, id, User.class);
    }

    @Override
    public List<User> findAll() {
        return FileIo.readAll(filename,User.class);
    }

    @Override
    public boolean existsById(UUID id) {
        String path = Path.RooT_PATH.getPath() + "/" + filename + "/" + id + ".sav";
        File file = new File(path);
        return file.exists();
    }

    @Override
    public void deleteById(UUID id) {
        String path = Path.RooT_PATH.getPath() + "/" + filename + "/" + id + ".sav";
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

}