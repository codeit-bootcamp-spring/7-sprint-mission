package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.dto.user.request.BinaryRequest;
import com.sprint.mission.discodeit.dto.user.response.BinaryResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.content.BinaryContent;
import com.sprint.mission.discodeit.entity.content.ContentsType;
import com.sprint.mission.discodeit.repository.BinaryRepository;
import com.sprint.mission.discodeit.service.file.FileIo;
import com.sprint.mission.discodeit.service.file.Path;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FileBinaryContentRepository implements BinaryRepository {
    private static final String filename = "binary";

    @Override
    public BinaryContent save(UUID binaryId, ContentsType contentsType, UUID contentID) {
        BinaryContent binary = new BinaryContent(binaryId,contentsType,contentID);
        FileIo.save(filename+"/"+contentsType, binary);
        return binary;

    }

    @Override
    public Optional<BinaryContent> findByUuid(UUID contentId,ContentsType contentsType) {

        return FileIo.read(filename +"/"+contentsType, contentId, BinaryContent.class);
    }

    @Override
    public void deleteByUuid(UUID contentId, ContentsType contentsType) {
        String path = Path.RooT_PATH.getPath() + "/" + filename +"/"+contentsType + "/" + contentId + ".sav";
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }
}
