package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class JCFBinaryContentRepository implements BinaryContentRepository {
    private final Map<UUID, BinaryContent> data = new ConcurrentHashMap<>();

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        Objects.requireNonNull(binaryContent, "binaryContent must not be null");
        data.put(binaryContent.getId(), binaryContent);
        return binaryContent;
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.ofNullable(data.get(Objects.requireNonNull(id)));
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        Objects.requireNonNull(ids, "id must not be null");
        if(ids.isEmpty()) {
            return List.of();
        }
        List<BinaryContent> out = new ArrayList<>(ids.size());
        for(UUID uuid : ids) {
            BinaryContent binaryContent = data.get(uuid);
            if(binaryContent != null) {
                out.add(binaryContent);
            }
        }
        return out.stream().toList();
    }

    @Override
    public boolean deleteById(UUID id) {
        return data.remove(Objects.requireNonNull(id)) != null;
    }
}
