package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;

import java.util.*;
import java.util.stream.Collectors;

public class JCFBinaryContentRepository implements BinaryContentRepository {

    private final Map<UUID, BinaryContent> data = new HashMap<>();

    @Override
    public BinaryContent save(BinaryContent b) {
        data.put(b.getId(), b);
        return b;
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<BinaryContent> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
    }

    //====================================

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        return ids.stream().map(data::get)
                .collect(Collectors.toList());
    }

}
