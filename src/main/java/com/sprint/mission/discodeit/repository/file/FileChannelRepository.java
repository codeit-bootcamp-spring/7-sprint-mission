package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.file.FileIo;
import com.sprint.mission.discodeit.service.file.Path;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

@Repository
public class FileChannelRepository implements ChannelRepository {

    private static final String filename = "channels";


    //로드 세이브
    //근데 잘해보면 한번에 할수있겟는데?
    @Override
    public Channel save(Channel channel) {
        FileIo.save(filename, channel);
        return channel;
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        return FileIo.read(filename, id, Channel.class);
    }

    @Override
    public List<Channel> findAll() {
        return FileIo.readAll(filename, Channel.class);
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