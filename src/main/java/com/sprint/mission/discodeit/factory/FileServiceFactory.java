package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;

public class FileServiceFactory implements ServiceFactory{
    private final String FILE_PATH = "C:\\Users\\user\\Workspace\\codeit-sprint-bootcamp\\7-sprint-mission\\data";
    private String USER_FILE = FILE_PATH + "\\users.sav";
    private String CHANNEL_FILE = FILE_PATH + "\\channels.sav";
    private String JOINED_FILE = FILE_PATH + "\\joinedChannels.sav";
    private String MESSAGE_FILE = FILE_PATH + "\\messages.sav";

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    private final UserService userService;
    private final ChannelService channelService;
    private final MessageService messageService;


    // 기본 경로의 파일에 데이터 저장
    public FileServiceFactory() {
        userRepository = new FileUserRepository(USER_FILE);
        messageRepository = new FileMessageRepository(MESSAGE_FILE);

        channelService = new FileChannelService(CHANNEL_FILE, JOINED_FILE);
        messageService = new FileMessageService(messageRepository);
        userService = new FileUserService(userRepository, messageRepository);
    }

    // 선택된 경로의 파일에 데이터 저장
    public FileServiceFactory(String USER_FILE, String CHANNEL_FILE, String JOINED_FILE, String MESSAGE_FILE) {
        this.USER_FILE = USER_FILE;
        this.CHANNEL_FILE = CHANNEL_FILE;
        this.JOINED_FILE = JOINED_FILE;
        this.MESSAGE_FILE = MESSAGE_FILE;

        userRepository = new FileUserRepository(USER_FILE);
        messageRepository = new FileMessageRepository(MESSAGE_FILE);

        channelService = new FileChannelService(CHANNEL_FILE, JOINED_FILE);
        messageService = new FileMessageService(messageRepository);
        userService = new FileUserService(userRepository, messageRepository);
    }

    public UserService getUserService() {
        return userService;
    }

    public ChannelService getChannelService() {
        return channelService;
    }

    public MessageService getMessageService() {
        return messageService;
    }
}
