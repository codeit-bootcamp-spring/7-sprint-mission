package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "jcf",
        matchIfMissing = false
)
public class JCFReadStatusRepository implements ReadStatusRepository {
    private final Map<UUID, ReadStatus> readStatusRepo;
    public JCFReadStatusRepository() {
        this.readStatusRepo = new HashMap<>();

    }

    @Override
    public void resetRepository() {
        readStatusRepo.clear();
    }

    @Override
    public ReadStatus createReadStatus(ReadStatus readStatus) {
        readStatusRepo.put(readStatus.getId(),readStatus);
        return readStatus;

    }

    @Override
    public void deleteReadStatus(UUID readStatusID) {
        readStatusRepo.remove(readStatusID);

    }

    @Override
    public void updateReadStatus(ReadStatus readStatus) {
        readStatusRepo.put(readStatus.getId(),readStatus);

    }

    @Override
    public Optional<ReadStatus> readReadStatus(UUID readStatusID) {
        return Optional.ofNullable(readStatusRepo.get(readStatusID));
    }

    @Override
    public List<ReadStatus> readAllReadStatus() {
        return readStatusRepo.values().stream().toList();
    }

    @Override
    public boolean isReadStatusExist(UUID userId, UUID channelId) {
        return readStatusRepo.containsKey(userId);
    }
}
