package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "jcf",
        matchIfMissing = false
)
public class JCFBinaryContentRepository implements BinaryContentRepository {
    private final Map <UUID,BinaryContent> binaryContentRepo;

    public JCFBinaryContentRepository() {
        this.binaryContentRepo = new HashMap<>();
    }

    @Override
    public BinaryContent createBinaryContent(BinaryContent binaryContent) {
        binaryContentRepo.put(binaryContent.getId(),binaryContent);
        return binaryContent;

    }

    @Override
    public Optional<BinaryContent> readBinaryContent(UUID binaryContentId) {
        return Optional.ofNullable(binaryContentRepo.get(binaryContentId));
    }

    @Override
    public List<BinaryContent> readAllBinaryContent() {
        return binaryContentRepo.values().stream().toList();
    }

    @Override
    public boolean isBinaryContentExist(UUID binaryContentId) {
        return binaryContentRepo.containsKey(binaryContentId);
    }

    @Override
    public void resetBinaryContentRepository() {
        binaryContentRepo.clear();
    }

    @Override
    public void deleteBinaryContent(UUID binaryContentId) {
        binaryContentRepo.remove(binaryContentId);

    }
}
