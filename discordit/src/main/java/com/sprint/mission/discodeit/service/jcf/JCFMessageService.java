package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;

public class JCFMessageService extends BasicMessageService {

    public JCFMessageService(MessageRepository messageRepository) {
        super(messageRepository);
    }
}
