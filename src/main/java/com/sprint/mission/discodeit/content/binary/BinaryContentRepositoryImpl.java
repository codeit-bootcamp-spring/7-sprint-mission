package com.sprint.mission.discodeit.content.binary;

import com.sprint.mission.discodeit.common.repository.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class BinaryContentRepositoryImpl extends BaseRepositoryImpl<BinaryContent, UUID> implements BinaryContentRepository {
    @Override
    public List<BinaryContent> findAllByOwnerId(UUID ownerId) {
        return dataMap.values().stream().filter(content -> content.getOwnerId().equals(ownerId)).toList();
    }

    @Override
    public List<BinaryContent> findAllByFilePath(String filePath) {
        return dataMap.values().stream().filter(content -> content.getFilePath().equals(filePath)).toList();
    }

    @Override
    public void deleteAllByOwnerId(UUID ownerId) {
        dataMap.values().removeIf(content -> content.getOwnerId().equals(ownerId));
    }
}
