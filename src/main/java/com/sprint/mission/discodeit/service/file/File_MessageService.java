package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.File_Common;
import com.sprint.mission.discodeit.repository.MesssageRepository;
import com.sprint.mission.discodeit.repository.file.File_MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class File_MessageService {
    public static final String FILE_PATH = File_Common.ROOT_PATH + "/File_MessageService.ser";
    public File file_MessageService = new File(FILE_PATH);
    private File_MessageService() {
        File_Common.fileCreate(file_MessageService, File_Common.ROOT_PATH);
    }
    private static File_MessageService service = new File_MessageService();
    static public File_MessageService getInstance() { return service; }

    private List<Message> messageList = new ArrayList<>();
    private File_MessageRepository messageRepository = new File_MessageRepository();

    //===============================
    //========== @Override ==========
    //===============================

    public Message neoMessage(String msg) {
        Message message = new Message(msg);
        messageList.add(message);
        messageRepository.messageWrite(messageList, "createMessage: [" + message + "]");
        return message;
    }
}
