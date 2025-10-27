package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.dto.user.request.BinaryRequest;
import com.sprint.mission.discodeit.dto.user.response.BinaryResponse;
import com.sprint.mission.discodeit.entity.content.BinaryContent;
import com.sprint.mission.discodeit.entity.content.ContentsType;
import com.sprint.mission.discodeit.repository.BinaryRepository;
import com.sprint.mission.discodeit.service.file.FileIo;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class FileBinaryContentRepository implements BinaryRepository {
    private static final String filename = "binary";

    @Override
    public BinaryContent save(UUID binaryId, ContentsType contentsType, String contentID) {
        BinaryContent binary = new BinaryContent(binaryId,contentsType,contentID);
        FileIo.save(filename+"/"+contentsType, binary);
        return binary;

    }

    @Override
    public Optional<BinaryContent> findByUuid(UUID contentId) {
        return Optional.empty();
    }

    @Override
    public void deleteByUserId(UUID contentId, ContentsType contentsType) {

    }
}
