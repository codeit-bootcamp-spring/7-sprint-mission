package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.dto.user.request.BinaryRequest;
import com.sprint.mission.discodeit.dto.user.response.BinaryResponse;
import com.sprint.mission.discodeit.entity.content.BinaryContent;
import com.sprint.mission.discodeit.entity.content.ContentsType;
import com.sprint.mission.discodeit.repository.BinaryRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

//@Repository("JCFBinary")
public class JCFBinaryContentRepository implements BinaryRepository {


    private final Map<UUID, BinaryContent> data;

    public JCFBinaryContentRepository() {
        this.data = new HashMap<>();
    }


    @Override
    public BinaryContent save(BinaryContent binaryContent) {

        this.data.put(binaryContent.getTypeUUID(),binaryContent);
        return binaryContent;

    }
    @Override
    public Optional<BinaryContent> find(UUID binaryId) {
        return Optional.empty();
    }

    @Override
    public Optional<BinaryContent> findByUuid(UUID contentId,ContentsType contentsType) {
        return Optional.ofNullable(this.data.get(contentId));
    }

    @Override
    public List<BinaryContent> findAll() {
        return List.of();
    }

    @Override
    public void deleteByUuid(UUID contentId, ContentsType contentsType) {
         data.remove(contentId);
    }
}
