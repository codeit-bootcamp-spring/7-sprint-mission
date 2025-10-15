package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.DeletedChannelDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ChannelService;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.ValidateService;
import com.sprint.mission.discodeit.service.file.FileUserService;

import java.util.function.BiConsumer;

import static com.sprint.mission.discodeit.static_.StaticString.*;

public class JCFChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ValidateService validateService;
    private final UserRepository userRepository;

    public JCFChannelService(ChannelRepository channelRepository, ValidateService validateService, UserRepository userRepository) {
        this.channelRepository = channelRepository;
        this.validateService = validateService;
        this.userRepository = userRepository;
    }

    @Override
    public void createChannel(ChannelDto channelDto) {
        if(validateService.isValidateChannel(channelDto)){
            System.out.println(VALIDATE_FAIL);
            return;
        }
        channelRepository.saveChannel(channelDto);
        System.out.println(CREATE_CHANNEL + channelDto.getName());


    }

    @Override
    public void readChannel(ChannelDto channelDto) {
        if(validateService.isValidateChannel(channelDto)){
            System.out.println(VALIDATE_FAIL);
            return;
        }
        ChannelDto tempChannel = channelRepository.getChannel(channelDto);
        System.out.println(tempChannel.toString());


    }

    @Override
    public void readAllChannel() {
        for(ChannelDto channelDto : channelRepository.getAllChannel()){
            System.out.println(channelDto.toString());
        }

    }

    @Override
    public void deleteChannel(ChannelDto channelDto) {
        if(channelDto == null){
            System.out.println(NULL_INPUT);
            return;
        }
        if(validateService.isValidateChannel(channelDto)){
            System.out.println(VALIDATE_FAIL);
            return;
        }
        channelRepository.deleteChannel(channelDto);
        System.out.println(DELETE_CHANNEL + channelDto.getName());
        return;


    }

    @Override
    public <T> void updateChannel(ChannelDto channelDto, Channel.channelElement channelElement, T updatedContent) {
        if(channelDto == null || updatedContent == null || channelElement == null){
            System.out.println(NULL_INPUT);
            return;
        }
        if(validateService.isValidateChannel(channelDto)){
            System.out.println(VALIDATE_FAIL);
        }

        try
        {


            channelRepository.updateChannel(channelDto, channelElement, updatedContent);
            System.out.printf("Update Channel field .: %s\n", channelDto.getName());
            System.out.println("Updated field: "+ channelElement.name() +"updatedContent : "+updatedContent);

        } catch (ClassCastException e) {

            System.out.println(WRONG_TYPE);
        }


    }

    @Override
    public void readUpdatedChannel() {
        if(channelRepository.getUpdatedChannel().length == 0){
            System.out.println(CHANNEL_EMPTY);
            return;
        }
        for(ChannelDto channelDto : channelRepository.getUpdatedChannel()){
            System.out.println(channelDto.toString());
        }

    }

    @Override
    public void readDeletedChannel() {
        if(channelRepository.getDeletedChannel().length == 0){
            System.out.println("No deleted channel");
            return;
        }
        System.out.println("===Deleted Channel=== ");
        for(DeletedChannelDto deletedChannelDto : channelRepository.getDeletedChannel()){
            System.out.println(deletedChannelDto.toString());
        }
        System.out.println("=============");

    }

    @Override
    public void inviteUserToChannel(UserDto userDto, ChannelDto channelDto) {
    if(userDto==null || channelDto==null){
        System.out.println(NULL_INPUT);
        return;
    }
    if(validateService.isValidateUser(userDto) || validateService.isValidateChannel(channelDto)){
        System.out.println(VALIDATE_FAIL);
        UserDto tempUser = userRepository.getUser(userDto);
        ChannelDto tempChannel = channelRepository.getChannel(channelDto);

        channelRepository.addUserToChannel(tempUser,tempChannel);
        userRepository.addChannelToUser(tempUser,tempChannel);
        System.out.println(tempUser.getName()+" is invited from "+tempChannel.getName());
        return;
    }


    }

    @Override
    public void deleteUserFromChannel(UserDto userDto, ChannelDto channelDto) {
        if(userDto==null || channelDto==null){
            System.out.println(NULL_INPUT);
            return;
        }
        if(!validateService.isValidateChannel(channelDto)){
            System.out.println(CHANNEL_NOT_EXIST + channelDto.getName());
            return;
        }
        if(!validateService.isValidateUser(userDto)){
            System.out.println(USER_NOT_EXIST + userDto.getName());
            return;
        }
        UserDto tempUser = userRepository.getUser(userDto);
        ChannelDto tempChannel = channelRepository.getChannel(channelDto);

        if(tempChannel.getUserDtoList().stream().noneMatch(x->x.getId().equals(tempUser.getId()))){
            System.out.println("Not in this channel");
            return;
        }
        channelRepository.deleteUserFromChannel(tempUser,tempChannel );
        userRepository.deleteChannelFromUser(tempUser,tempChannel );
        System.out.println(userDto.getName()+" is banished from "+channelDto.getName());




    }
}
