package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.base.Message;
import com.sprint.mission.discodeit.entity.base.Receivable;
import com.sprint.mission.discodeit.entity.base.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;

import java.util.List;

public class JCFMessageService extends BasicMessageService {

    public JCFMessageService(MessageRepository messageRepository) {
        super(messageRepository);
    }
}
