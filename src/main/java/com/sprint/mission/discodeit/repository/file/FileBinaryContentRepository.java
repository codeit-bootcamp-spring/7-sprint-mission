package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.dto.user.request.BinaryRequest;
import com.sprint.mission.discodeit.dto.user.response.BinaryResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.content.BinaryContent;
import com.sprint.mission.discodeit.entity.content.ContentsType;
import com.sprint.mission.discodeit.repository.BinaryRepository;
import com.sprint.mission.discodeit.service.file.FileIo;
import com.sprint.mission.discodeit.service.file.Path;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "file",
        matchIfMissing = true
)
public class FileBinaryContentRepository implements BinaryRepository {
    private static final String filename = "binary";

    @Override
    public BinaryContent save(BinaryContent binary) {
        FileIo.save(filename, binary);
        return binary;

    }

    @Override
    public Optional<BinaryContent> find(UUID binaryId) {
        return FileIo.read(filename, binaryId, BinaryContent.class);
    }

    @Override
    public List<BinaryContent> findAll() {
        return FileIo.readAll(filename, BinaryContent.class);
    }



    @Override
    public void delete(UUID contentId) {
        String path = Path.RooT_PATH.getPath() + "/" + filename+"/"  + contentId + ".sav";
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }
}
