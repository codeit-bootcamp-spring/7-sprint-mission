package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;

import java.util.*;

import static com.sprint.mission.discodeit.global.utils.FileIOHandler.*;

public class FileBinaryContentRepository implements BinaryContentRepository {
    private final Map<UUID, BinaryContent> contentStore = new HashMap<>();
    private final String filePath;

    public FileBinaryContentRepository(String filePath) {
        this.filePath = filePath;
        loadFromFile(filePath, contentStore);
    }

    @Override
    public void save(BinaryContent binaryContent) {
        contentStore.put(binaryContent.getId(), binaryContent);
        saveToFile(filePath, contentStore);
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.ofNullable(contentStore.get(id));
    }

    @Override
    public List<BinaryContent> findAll() {
        return new ArrayList<>(contentStore.values());
    }

    @Override
    public void delete(UUID id) {
        contentStore.remove(id);
        saveToFile(filePath, contentStore);
    }

    @Override
    public void deleteByIds(List<UUID> idList) {
        idList.forEach(id -> {
            contentStore.remove(id);
        });

        saveToFile(filePath, contentStore);
    }
}
