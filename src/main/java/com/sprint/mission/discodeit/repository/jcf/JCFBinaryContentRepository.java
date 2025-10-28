package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.dto.user.request.BinaryRequest;
import com.sprint.mission.discodeit.dto.user.response.BinaryResponse;
import com.sprint.mission.discodeit.entity.content.BinaryContent;
import com.sprint.mission.discodeit.entity.content.ContentsType;
import com.sprint.mission.discodeit.repository.BinaryRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository("JCFBinary")
public class JCFBinaryContentRepository implements BinaryRepository {

    private final Map<UUID, BinaryContent> data;

    public JCFBinaryContentRepository() {
        this.data = new HashMap<>();
    }


    @Override
    public BinaryContent save(UUID binaryId, ContentsType contentsType, String contentID) {
        BinaryContent binary = new BinaryContent(binaryId,contentsType,contentID);
       //여기서 바이너리로 하면  그 고유이긴한데
        this.data.put(binary.getTypeUUID(),binary);
        return binary;

    }

    @Override
    public Optional<BinaryContent> findByUuid(UUID contentId,ContentsType contentsType) {
        return Optional.ofNullable(this.data.get(contentId));
    }

    @Override
    public void deleteByUuid(UUID contentId, ContentsType contentsType) {
         data.remove(contentId);
    }
}
