package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;

public class FileServiceFactory implements ServiceFactory{
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;

    private final UserService userService;
    private final ChannelService channelService;
    private final MessageService messageService;

    // 선택된 경로의 파일에 데이터 저장
    public FileServiceFactory(String userFilePath, String channelFilePath, String joinedFilePath, String messageFilePath) {
        userRepository = new FileUserRepository(userFilePath);
        channelRepository = new FileChannelRepository(channelFilePath, joinedFilePath);
        messageRepository = new FileMessageRepository(messageFilePath);
        userService = new FileUserService(userRepository, messageRepository);
        channelService = new FileChannelService(channelRepository);
        messageService = new FileMessageService(messageRepository);
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
