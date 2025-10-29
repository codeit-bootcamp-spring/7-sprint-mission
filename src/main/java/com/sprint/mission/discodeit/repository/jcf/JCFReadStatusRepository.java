package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JCFReadStatusRepository implements ReadStatusRepository {
    Map<UUID, ReadStatus> readStatusStore = new HashMap<>();

    @Override
    public void save(ReadStatus readStatus) {
        readStatusStore.put(readStatus.getId(), readStatus);
    }

    @Override
    public ReadStatus findById(UUID id) {
        return Optional.ofNullable(readStatusStore.get(id))
                .orElseThrow(() -> new IllegalStateException("저장된 정보가 없습니다."));
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        readStatusStore.values().removeIf(s -> s.getChannelId().equals(channelId));
    }

    @Override
    public void deleteByChannelMember(UUID channelId, UUID memberId) {
        readStatusStore.values().removeIf(s -> s.getChannelId().equals(channelId) &&  s.getUserId().equals(memberId));
    }
}
