package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.File_Common;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MesssageRepository;
import com.sprint.mission.discodeit.service.file.File_MessageService;

import java.util.List;
import java.util.UUID;

public class File_MessageRepository implements MesssageRepository {

    @Override
    public void messageWrite(List<Message> messageList, String msg) {
        File_Common.fileWrite(messageList, File_MessageService.FILE_PATH, msg);
    }
}
