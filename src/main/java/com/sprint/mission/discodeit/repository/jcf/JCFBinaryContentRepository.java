package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class JCFBinaryContentRepository implements BinaryContentRepository {

    private final Map<UUID, BinaryContent> binaryContents = new ConcurrentHashMap<>();

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        binaryContents.put(binaryContent.getId(), binaryContent);
        return binaryContent;
    }

    @Override
    public Optional<BinaryContent> findById(UUID uuid) {
        return Optional.ofNullable(binaryContents.get(uuid));
    }

    @Override
    public List<BinaryContent> findAll() {
        return new ArrayList<>(binaryContents.values());
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> BinaryContentIds) {
        Collection<BinaryContent> contents = binaryContents.values();
               return contents.stream()
                .filter(content -> BinaryContentIds.contains(content.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID uuid) {
        binaryContents.remove(uuid);
    }
}
