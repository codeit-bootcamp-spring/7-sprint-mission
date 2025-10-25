package com.sprint.mission.discodeit.binarycontent.infrastructure;

import com.sprint.mission.discodeit.binarycontent.application.BinaryContentRepository;
import com.sprint.mission.discodeit.binarycontent.domain.BinaryContent;
import com.sprint.mission.discodeit.readstatus.domain.ReadStatus;

import java.util.*;

public class JCFBinaryContentRepository implements BinaryContentRepository {

    private final Map<UUID, BinaryContent> store = new HashMap<>();

    @Override
    public void save(BinaryContent binaryContent) {
        UUID key = binaryContent.getId();
        store.put(key,binaryContent);
    }

    @Override
    public void remove(BinaryContent binaryContent) {
        UUID key = binaryContent.getId();
        store.remove(key);
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<BinaryContent> findAll() {
        return List.copyOf(store.values());
    }
}
