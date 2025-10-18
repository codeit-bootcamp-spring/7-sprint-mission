package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.*;
import java.util.*;

public class FileMessageRepository extends AbstractFileRepository<Message, UUID> implements MessageRepository {

    private static final String MSG_FILE_PATH = "messages.ser";

    @Override
    protected String getFilePath() {
        return MSG_FILE_PATH;
    }

    @Override
    protected UUID getId(Message message) {
        return message.getId();
    }
}
