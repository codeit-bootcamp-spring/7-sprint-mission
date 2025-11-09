package com.sprint.mission.discodeit.common.exceptions.binaryContent;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.UUID;

public class BinaryContentNotFoundException extends RuntimeException {
    public BinaryContentNotFoundException(BinaryContent content) {
        super("존재하지 않는 파일입니다 : \n" +
                "\tUUID: " + content.getId() +
                "\turl: " + content.getFileUrl());
    }

    public BinaryContentNotFoundException(UUID id) {
        super("존재하지 않는 파일입니다 : \n" +
                "\tUUID: " + id);
    }
}
