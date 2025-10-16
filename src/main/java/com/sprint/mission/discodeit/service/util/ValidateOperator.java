package com.sprint.mission.discodeit.service.util;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.service.ChannelRepository;
import com.sprint.mission.discodeit.service.MessageRepository;
import com.sprint.mission.discodeit.service.UserRepository;
import com.sprint.mission.discodeit.service.ValidateService;

import java.util.Arrays;
import java.util.List;

public class ValidateOperator implements ValidateService {

    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    public ValidateOperator(ChannelRepository channelRepository, UserRepository userRepository, MessageRepository messageRepository) {
        this.channelRepository = channelRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public boolean isValidateChannel(ChannelDto channelDto) {
       if(channelDto==null) return false;
        ChannelDto[] channelDtos = channelRepository.getAllChannel();
       return Arrays.stream(channelDtos).anyMatch(x->x.getId().equals(channelDto.getId()));
    }

    @Override
    public boolean isValidateUser(UserDto userDto) {
        if(userDto==null) return false;
        UserDto[] userDtos = userRepository.getAllUser();
        return Arrays.stream(userDtos).anyMatch(x->x.getId().equals(userDto.getId()));
    }

    @Override
    public boolean isValidateMessage(MessageDto messageDto) {
        if(messageDto==null) return false;
        MessageDto[] messageDtos = messageRepository.getAllMessage();
        return Arrays.stream(messageDtos).anyMatch(x->x.getId().equals(messageDto.getId()));

    }
}
