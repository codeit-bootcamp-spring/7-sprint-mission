package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.File_Common;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class File_MessageService {
    private static final String FILE_PATH = File_Common.ROOT_PATH + "/File_MessageService.txt";
    public File file_MessageService = new File(FILE_PATH);
    private List<Message> messageList = new ArrayList<>();

    private static File_MessageService service = new File_MessageService();

    private File_MessageService() {
        File_Common.fileCreate(file_MessageService, File_Common.ROOT_PATH);
    }
    static public File_MessageService getInstance() { return service; }

    public Message createMessage(String msg) {
        Message message = new Message(msg);
        messageList.add(message);

        File_Common.fileWrite(messageList, FILE_PATH, "createMessage: [" + message + "]");

        return message;
    }

    //===============================
    //========== @Override ==========
    //===============================

}
