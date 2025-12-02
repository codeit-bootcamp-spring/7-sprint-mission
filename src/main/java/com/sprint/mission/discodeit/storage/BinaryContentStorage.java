package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binarycontent.response.BinaryContentInfoRes;
import java.io.InputStream;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

public interface BinaryContentStorage {

  UUID put(UUID binaryId, byte[] data);

  InputStream get(UUID binaryId);

  ResponseEntity<?> download(BinaryContentInfoRes dto);

  void delete(UUID binaryId);
}
