package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.dto.binaryContent.request.BinaryContentCreateReq;
import com.sprint.mission.discodeit.entity.BinaryContent;

public class BinaryContentFactory {
    private BinaryContentFactory(){}

    public static BinaryContent create(BinaryContentCreateReq req){
        return BinaryContent.builder()
                .data(req.data())
                .fileName(req.fileName())
                .fileType(req.fileType())
                .build();

    }
}
