package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;

public class FileMessageService extends BasicMessageService {
    public FileMessageService(MessageRepository messageRepository) {
        super(messageRepository);
    }
}
