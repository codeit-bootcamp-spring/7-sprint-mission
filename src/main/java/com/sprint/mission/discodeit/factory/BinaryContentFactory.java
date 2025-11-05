package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.dto.binarycontent.request.BinaryContentCreateReq;
import com.sprint.mission.discodeit.dto.binarycontent.request.BinaryContentUpdateReq;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.UUID;

public class BinaryContentFactory {
    private BinaryContentFactory(){}

    public static BinaryContent create(BinaryContentCreateReq req) {
        return create(req.data(), req.fileName(), req.fileType());
    }

    public static BinaryContent create(BinaryContentUpdateReq req) {
        return create(req.data(), req.fileName(), req.fileType());
    }

    public static BinaryContent create(byte[] data, String fileName, String fileType){
        return new BinaryContent(data, fileName, fileType);
    }
}
