package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.dto.binarycontent.request.BinaryContentCreateReq;
import com.sprint.mission.discodeit.entity.BinaryContent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BinaryContentFactory {

  public static BinaryContent create(BinaryContentCreateReq req) {
    long size = req.data() == null ? 0 : req.data().length;
    return create(req.fileName(), req.fileType(), size);
  }

  public static BinaryContent create(String fileName, String fileType, long size) {
    return BinaryContent.create(fileName, fileType, size);
  }
}
