package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.InterfaceBinaryContentRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FileBinaryContentRepository implements InterfaceBinaryContentRepository {
    private final FileUtil fileUtil;
    public FileBinaryContentRepository(@Qualifier("binaryContentFileUtil") FileUtil fileUtil) {
        this.fileUtil = fileUtil;
    }

    public void save(BinaryContent content) {
        fileUtil.saveRepository(content);
    }

    public Optional<BinaryContent> findById(UUID binaryContentId) {
        Optional<BinaryContent> optionalUserContents = fileUtil.findAll().stream()
            .map(userContent -> (BinaryContent) userContent)
            .filter(userContent -> userContent.getId().equals(binaryContentId))
            .findFirst();

        return optionalUserContents;
    }

    public List<BinaryContent> findAll() {
        return fileUtil.findAll().stream().map(userContent -> (BinaryContent) userContent).toList();
    }

    @Override
    public boolean existsById(UUID id) {
        return false;
    }

    @Override
    public boolean existsByName(String name) {
        return false;
    }

    public boolean deleteById(UUID binaryContentId) {
        return fileUtil.deleteRepository(binaryContentId);
    }
}
