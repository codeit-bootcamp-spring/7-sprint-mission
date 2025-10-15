package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.*;

import java.util.Arrays;
import java.util.List;

import static com.sprint.mission.discodeit.static_.StaticString.*;

public class JCFUserService implements UserService {
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final ValidateService validateService;
    private final MessageRepository messageRepository;

    public JCFUserService(UserRepository userRepository, ChannelRepository channelRepository, ValidateService validateService, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
        this.validateService = validateService;
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
        UserDto[] userList = userRepository.getAllUser();

        for(UserDto tmp : userList){
            if(tmp.getChannelDtoList().size() > 0){
                for(ChannelDto channelDto : tmp.getChannelDtoList()){
                    channelRepository.deleteUserFromChannel(tmp,channelDto);
                }
            }
        }
        MessageDto [] messageList = messageRepository.getAllMessage();
        List<MessageDto> messageDtoList = Arrays.stream(messageList).filter(x-> x.getSender().getId()==userDto.getId()).toList();
        messageDtoList.forEach(x->messageRepository.setDefaultSender(x));






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
        for (UserDto userDto : userRepository.getDeletedUser()) {
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
        }
        UserDto tempUserDto = userRepository.getUser(userDto);
        ChannelDto tempChannelDto = channelRepository.getChannel(channelDto);
        if(!tempChannelDto.getUserDtoList().stream().anyMatch(x->x.getId().equals(tempUserDto.getId()))){
            System.out.println("Not in this channel");
            return;
        }
        channelRepository.deleteUserFromChannel(tempUserDto,tempChannelDto);
        userRepository.deleteChannelFromUser(tempUserDto,tempChannelDto);
        System.out.println(tempUserDto.getName()+" exits "+tempChannelDto.getName()+" channel.");
    }
}
