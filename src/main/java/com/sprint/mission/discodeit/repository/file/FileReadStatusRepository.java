package com.sprint.mission.discodeit.repository.file;

import java.io.File;
import java.util.*;

import com.sprint.mission.discodeit.entity.status.ReadStatus;
import com.sprint.mission.discodeit.repository.FileIo;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "file",
        matchIfMissing = true
)
public class FileReadStatusRepository implements ReadStatusRepository {

    private static final String filename = "readStatus";

    @Override
    public ReadStatus save(ReadStatus readStatus) {

        FileIo.save(filename, readStatus, readStatus.getId());
        return readStatus;
    }

    @Override
    public ReadStatus find(UUID readStatusId) {
        return FileIo.read(filename, readStatusId, ReadStatus.class)
                .orElseThrow(() -> new NoSuchElementException("Id를 찾을 수 없어: " + readStatusId));
    }

    @Override
    public List<ReadStatus> findAll() {
        return FileIo.readAll(filename, ReadStatus.class);
    }

    @Override
    public Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId) {
        return FileIo.readAll(filename, ReadStatus.class).stream()
                .filter(rs -> rs.getUserId().equals(userId)
                        && rs.getChannelId().equals(channelId))
                .findFirst();
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return FileIo.readAll(filename, ReadStatus.class).stream()
                .filter(rs -> rs.getUserId().equals(userId))
                .toList();
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        return FileIo.readAll(filename, ReadStatus.class).stream()
                .filter(rs -> rs.getChannelId().equals(channelId))
                .toList();
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
