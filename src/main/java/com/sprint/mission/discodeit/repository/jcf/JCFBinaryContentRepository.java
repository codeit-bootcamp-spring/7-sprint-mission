package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.dto.user.request.BinaryRequest;
import com.sprint.mission.discodeit.dto.user.response.BinaryResponse;
import com.sprint.mission.discodeit.entity.content.BinaryContent;
import com.sprint.mission.discodeit.entity.content.ContentsType;
import com.sprint.mission.discodeit.repository.BinaryRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "jcf"
)
public class JCFBinaryContentRepository implements BinaryRepository {


    private final Map<UUID, BinaryContent> data;

    public JCFBinaryContentRepository() {
        this.data = new ConcurrentHashMap<>();
    }


    @Override
    public BinaryContent save(BinaryContent binaryContent) {

        this.data.put(binaryContent.getId(),binaryContent);
        return binaryContent;

    }


    @Override
    public Optional<BinaryContent> find(UUID binaryId) {
        return Optional.ofNullable(this.data.get(binaryId));
    }


    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        return this.data.values().stream()
                .filter(content -> ids.contains(content.getId()))
                .toList();
    }

    @Override
    public List<BinaryContent> findAll() {
        return List.of();
    }

    @Override
    public void delete(UUID binaryId) {
         data.remove(binaryId);
    }
}
