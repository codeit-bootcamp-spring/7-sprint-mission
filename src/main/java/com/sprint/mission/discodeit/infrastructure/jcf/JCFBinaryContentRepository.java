package com.sprint.mission.discodeit.infrastructure.jcf;

import com.sprint.mission.discodeit.domain.binarycontent.BinaryContentRepository;
import com.sprint.mission.discodeit.domain.binarycontent.BinaryContent;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(prefix = "discodeit.repository", name = "type", havingValue = "jcf")
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
