package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;

import java.util.*;

public class JCFBinaryContentRepository implements BinaryContentRepository {
    private final Map<UUID, BinaryContent> contentStore = new HashMap<>();

    @Override
    public void save(BinaryContent binaryContent) {
        contentStore.put(binaryContent.getId(), binaryContent);
    }

    @Override
    public BinaryContent findById(UUID id) {
        return Optional.ofNullable(contentStore.get(id))
                .orElseThrow(() -> new IllegalStateException("이미지 또는 파일이 존재하지 않습니다."));
    }

    @Override
    public List<BinaryContent> findAll() {
        return new ArrayList<>(contentStore.values());
    }

    @Override
    public void delete(UUID id) {
        contentStore.remove(id);
    }

    @Override
    public void deleteByIds(List<UUID> idList) {
        idList.forEach(id -> {
            contentStore.remove(id);
        });
    }
}
