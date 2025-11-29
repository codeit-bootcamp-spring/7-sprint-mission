package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.dto.binarycontent.request.BinaryContentCreateReq;
import com.sprint.mission.discodeit.dto.binarycontent.request.BinaryContentUpdateReq;
import com.sprint.mission.discodeit.entity.BinaryContent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BinaryContentFactory {

  public static BinaryContent create(BinaryContentCreateReq req) {
    return create(req.data(), req.fileName(), req.fileType());
  }

  public static BinaryContent create(BinaryContentUpdateReq req) {
    return create(req.data(), req.fileName(), req.fileType());
  }

  public static BinaryContent create(byte[] data, String fileName, String fileType) {
    return BinaryContent.create(data, fileName, fileType);
  }
}
