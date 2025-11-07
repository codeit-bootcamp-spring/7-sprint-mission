package com.sprint.mission.discodeit.common.exceptions;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.UUID;

public class ContentNotFoundException extends RuntimeException {
    public ContentNotFoundException(BinaryContent content) {
        super("존재하지 않는 파일입니다 : \n" +
                "\tUUID: " + content.getId() +
                "\turl: " + content.getFileUrl());
    }

    public ContentNotFoundException(UUID id) {
        super("존재하지 않는 파일입니다 : \n" +
                "\tUUID: " + id);
    }
}
