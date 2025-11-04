package com.sprint.mission.discodeit.repository.file;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.entity.Message;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.*;

@Repository
@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "file",
        matchIfMissing = true
)
public class FileMessageRepository implements MessageRepository {

    private static final String filename = "messages";



    //로드 세이브
    //근데 잘해보면 한번에 할수있겟는데?
    @Override
    public Message save(Message message) {
        FileIo.save(filename, message);
        return message;
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return FileIo.read(filename, id, Message.class);
    }

    @Override
    public List<Message> findAll() {
        return FileIo.readAll(filename, Message.class);
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