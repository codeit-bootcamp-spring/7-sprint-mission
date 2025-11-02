package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.global.util.file.FileManager;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.util.*;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileBinaryContentRepository implements BinaryContentRepository {
    private final Path filePath;
    private final Map<UUID, BinaryContent> binaryContents;

    public FileBinaryContentRepository(@Value("${file.path.binaryContentPath}") Path binaryContentFilePath) {
        this.filePath = binaryContentFilePath;
        FileManager.init(filePath);
        binaryContents = FileManager.readFile(filePath);
    }

    @Override
    public void save(BinaryContent binaryContent) {
        binaryContents.put(binaryContent.getId(), binaryContent);
        FileManager.writeFile(filePath, binaryContents);
    }

    @Override
    public Optional<BinaryContent> findById(UUID binaryContentId) {
        return Optional.ofNullable(binaryContents.get(binaryContentId));
    }

    @Override
    public List<BinaryContent> findAll() {
        return new ArrayList<>(binaryContents.values());
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds) {
        return binaryContents.values().stream()
                .filter(binaryContent -> binaryContentIds.contains(binaryContent.getId()))
                .toList();
    }

    @Override
    public void deleteById(UUID binaryContentId) {
        binaryContents.remove(binaryContentId);
        FileManager.writeFile(filePath, binaryContents);
    }

    @Override
    public boolean existsById(UUID binaryContentId) {
        return binaryContents.containsKey(binaryContentId);
    }
}
