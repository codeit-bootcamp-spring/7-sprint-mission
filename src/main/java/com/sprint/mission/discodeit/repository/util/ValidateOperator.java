package com.sprint.mission.discodeit.repository.util;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.ValidateService;
import com.sprint.mission.discodeit.repository.file.FileChannelService;
import com.sprint.mission.discodeit.repository.file.FileMessageService;
import com.sprint.mission.discodeit.repository.file.FileUserService;

import java.io.File;

public class ValidateOperator implements ValidateService {

    public ValidateOperator(ChannelRepository channelRepository, MessageRepository messageRepository, UserRepository userRepository) {
        this.channelRepository = channelRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;



    @Override
    public boolean isValidateChannel(ChannelDto channelDto) {
        if(channelDto==null) return false;

        return channelRepository.getChannel(channelDto)==null;
    }

    @Override
    public boolean isValidateUser(UserDto userDto) {
        if(userDto==null) return false;

        return userRepository.getUser(userDto)==null;
    }

    @Override
    public boolean isValidateMessage(MessageDto messageDto) {
        if(messageDto==null) return false;

        return messageRepository.getMessage(messageDto)==null;
    }
}
