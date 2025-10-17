package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.DeletedUserDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ValidateService;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.sprint.mission.discodeit.service.util.StaticString.*;
import static com.sprint.mission.discodeit.service.util.StaticString.CHANNEL_NOT_EXIST;
import static com.sprint.mission.discodeit.service.util.StaticString.DELETE_USER;
import static com.sprint.mission.discodeit.service.util.StaticString.NULL_INPUT;
import static com.sprint.mission.discodeit.service.util.StaticString.USER_EMPTY;
import static com.sprint.mission.discodeit.service.util.StaticString.USER_NOT_EXIST;
import static com.sprint.mission.discodeit.service.util.StaticString.WRONG_TYPE;

public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final ValidateService validateService;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;

    public BasicUserService(UserRepository userRepository, ValidateService validateService, ChannelRepository channelRepository, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.validateService = validateService;
        this.channelRepository = channelRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public void createUser(UserDto userDto) {
        if(userDto==null){
            System.out.println(NULL_INPUT);
            return;
        }
        if(validateService.isValidateUser(userDto)){
            System.out.println(USER_EXIST + userDto.getName());
            return;
        }

        userRepository.saveUser(userDto);
        System.out.println(CREATE_USER + userDto.getName());


    }

    @Override
    public void readUser(UserDto userDto) {
        if(userDto==null){
            System.out.println(NULL_INPUT);
            return;
        }
        if(!validateService.isValidateUser(userDto)){
            System.out.println(USER_NOT_EXIST + userDto.getName());
            return;
        }
        UserDto result = userRepository.getUser(userDto);
        System.out.println(result.toString());

    }

    @Override
    public void readAllUser() {
        if(userRepository.getAllUser().length == 0){
            System.out.println(USER_EMPTY);
            return;
        }
        for(UserDto userDto : userRepository.getAllUser()){
            System.out.println(userDto.toString());
        }

    }

    @Override
    public void deleteUser(UserDto userDto) {
        if(userDto==null){
            System.out.println(NULL_INPUT);
            return;
        }
        if(!validateService.isValidateUser(userDto)){
            System.out.println(USER_NOT_EXIST + userDto.getName());
            return;
        }
        ChannelDto [] channelList = channelRepository.getAllChannel();
        List<ChannelDto> channelDtoStream = Arrays.stream(channelList).filter(x -> x.getUserDtoList().stream().anyMatch(y -> y.getId().equals(userDto.getId()))).toList();
        for(ChannelDto channelDto : channelDtoStream) channelRepository.deleteUserFromChannel(userDto,channelDto);


        MessageDto[] messageList = messageRepository.getAllMessage();
        List<MessageDto> messageDtoList = Arrays.stream(messageList).filter(x-> x.getSender().getId().equals(userDto.getId())).collect(Collectors.toList());


        for(MessageDto temp : messageDtoList)
        {
            messageRepository.setDefaultSender(temp);

        }





        /// ///// message DB 작성
        userRepository.deleteUser(userDto);

        System.out.println(DELETE_USER + userDto.getName());

    }

    @Override
    public <T> void updateUser(UserDto userDto, User.userElement userElement, T updatedContent) {
        if(userDto == null || updatedContent == null || userElement == null){
            System.out.println(NULL_INPUT);
            return;
        }
        if(!validateService.isValidateUser(userDto)){
            System.out.println(USER_NOT_EXIST + userDto.getName());
            return;
        }
        try {
            userRepository.updateUser(userDto, userElement, updatedContent);
            System.out.println("User Updated: " + userDto.getName());
            System.out.println("Updated field: " + userElement.name() + "Updated Content: " + updatedContent );
        }
        catch (ClassCastException e){
            System.out.println(WRONG_TYPE);
            return;
        }



    }

    @Override
    public void readUpdatedUser() {
        if(userRepository.getUpdatedUser().length == 0){
            System.out.println("No Updated User");
            return;
        }
        System.out.println("===Updated User=== ");
        for(UserDto userDto : userRepository.getUpdatedUser()){
            System.out.println(userDto.toString());
        }
        System.out.println("================");


    }

    @Override
    public void readDeletedUser() {
        if (userRepository.getDeletedUser().length == 0) {
            System.out.println("No Deleted User");
            return;
        }
        System.out.println("===Deleted User=== ");
        for (DeletedUserDto userDto : userRepository.getDeletedUser()) {
            System.out.println(userDto.toString());
        }
        System.out.println("================");

    }

    @Override
    public void enterChannel(UserDto userDto, ChannelDto channelDto) {
        if(userDto==null || channelDto==null){
            System.out.println(NULL_INPUT);
            return;
        }
        if(!validateService.isValidateUser(userDto)){
            System.out.println(USER_NOT_EXIST + userDto.getName());
            return;
        }
        if(!validateService.isValidateChannel(channelDto)){
            System.out.println(CHANNEL_NOT_EXIST + channelDto.getName());
            return;
        }
        UserDto tempUserDto = userRepository.getUser(userDto);
        ChannelDto tempChannelDto = channelRepository.getChannel(channelDto);

        channelRepository.addUserToChannel(tempUserDto,tempChannelDto);
        userRepository.addChannelToUser(tempUserDto,tempChannelDto);
        System.out.println(tempUserDto.getName()+" enters "+tempChannelDto.getName()+" channel.");



    }

    @Override
    public void exitChannel(UserDto userDto, ChannelDto channelDto) {
        if(userDto==null || channelDto==null){
            System.out.println(NULL_INPUT);
            return;
        }
        if(!validateService.isValidateUser(userDto)){
            System.out.println(USER_NOT_EXIST + userDto.getName());
            return;
        }
        if(!validateService.isValidateChannel(channelDto)){
            System.out.println(CHANNEL_NOT_EXIST + channelDto.getName());
            return;
        }
        UserDto tempUserDto = userRepository.getUser(userDto);
        ChannelDto tempChannelDto = channelRepository.getChannel(channelDto);
        if(tempChannelDto.getUserDtoList().stream().noneMatch(x->x.getId().equals(tempUserDto.getId()))){
            System.out.println("Not in this channel");
            return;
        }
        channelRepository.deleteUserFromChannel(tempUserDto,tempChannelDto);
        userRepository.deleteChannelFromUser(tempUserDto,tempChannelDto);
        System.out.println(tempUserDto.getName()+" exits "+tempChannelDto.getName()+" channel.");
    }
}
