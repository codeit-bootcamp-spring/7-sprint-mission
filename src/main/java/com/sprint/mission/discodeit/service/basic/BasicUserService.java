package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.dto.request.binaryContent.ProfileCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.binaryContent.ProfileUpdateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.UserReadResponseDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.entityElement.BinaryContentUsage;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

import static com.sprint.mission.discodeit.service.util.StaticString.*;
import static com.sprint.mission.discodeit.service.util.StaticString.USER_NOT_EXIST;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;


    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public User createUser(UserCreateRequestDto userCreateRequestDto) {
        User user = User.builder()
                .userName(userCreateRequestDto.getUserName())
                .name(userCreateRequestDto.getName())
                .email(userCreateRequestDto.getEmail())
                .password(userCreateRequestDto.getPassword()).build();

        UserStatus userStatus = UserStatus.builder()
                .userId(user.getId())
                .lastOnlineTime(Instant.now())
                .build();

        userNameEmailCheck(user);

        userStatusRepository.createUserStatus(userStatus);
        return userRepository.saveUser(user);


    }

    @Override
    public User createUser(UserCreateRequestDto userCreateRequestDto, ProfileCreateRequestDto profileCreateRequestDtoDto) {

        BinaryContent binaryContent = BinaryContent.builder()
                .binaryFile(profileCreateRequestDtoDto.getFile())
                .binaryContentUsage(BinaryContentUsage.PROFILE)
                .build();

        User user = User.builder()
                .userName(userCreateRequestDto.getUserName())
                .name(userCreateRequestDto.getName())
                .email(userCreateRequestDto.getEmail())
                .profileId(binaryContent.getId())
                .password(userCreateRequestDto.getPassword())
                .build();

        UserStatus userStatus = UserStatus.builder()
                .userId(user.getId())
                .lastOnlineTime(Instant.now())
                .build();

        userNameEmailCheck(user);

        binaryContentRepository.createBinaryContent(binaryContent);
        userRepository.saveUser(user);
        userStatusRepository.createUserStatus(userStatus);
        return user;
    }

    @Override
    public UserReadResponseDto readUser(UUID userID) {
        User user = userRepository.getUserById(userID).orElseThrow(() -> new IllegalArgumentException(USER_NOT_EXIST));
        UserStatus userStatus = userStatusRepository.readAllUserStatus()
                .stream()
                .filter(x -> x.getUserId().equals(userID)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException(USER_NOT_EXIST));

        return UserReadResponseDto.of(user, userStatus);
    }

    @Override
    public List<UserReadResponseDto> readAllUser() {
        return userRepository.getAllUser().stream().map(x ->
                readUser(x.getId())).toList();
    }

    @Override
    public void deleteUser(UUID userId) {

        List<Channel> channelList = channelRepository.getAllChannel();
        List<Message> messageList = messageRepository.getAllMessage();
        List<UserStatus> userStatusList = userStatusRepository.readAllUserStatus();
        if (binaryContentRepository.isBinaryContentExist(userId)) {
            binaryContentRepository.deleteBinaryContent(userId);
        }
        UserStatus targetUserStatus = userStatusList.stream().filter(x -> x.getUserId().equals(userId)).findFirst().orElseThrow(() -> new IllegalArgumentException(USER_NOT_EXIST));
        userStatusRepository.deleteUserStatus(targetUserStatus.getId());
        channelList.stream().filter(x ->
                        x.getJoinUserList()
                                .removeIf(y -> y.equals(userId))
                )
                .forEach(channelRepository::updateChannel);
        messageList.stream().filter(
                        x -> x.getSenderId().equals(userId))
                .forEach
                        (s -> s.setSenderId(DEFAULT_SENDER.getId()));
        userRepository.deleteUser(userId);

    }

    @Override
    public <T> void updateUser(UserUpdateRequestDto<T> userUpdateRequestDto) {
        User user = userRepository.getUserById(userUpdateRequestDto.getUserId()).orElseThrow(() -> new IllegalArgumentException(USER_NOT_EXIST));
        BiConsumer<User, T> biConsumer = (BiConsumer<User, T>) userUpdateRequestDto.getType().setter;
        biConsumer.accept(user, userUpdateRequestDto.getUpdateValue());
        user.updateEntity();
        userRepository.updateUser(user);
    }

    @Override
    public <T> void updateUser(UserUpdateRequestDto<T> userUpdateRequestDto, ProfileUpdateRequestDto profileUpdateRequestDto) {
        User user = userRepository.getUserById(userUpdateRequestDto.getUserId()).orElseThrow(() -> new IllegalArgumentException(USER_NOT_EXIST));
        BiConsumer<User, T> biConsumer = (BiConsumer<User, T>) userUpdateRequestDto.getType().setter;
        biConsumer.accept(user, userUpdateRequestDto.getUpdateValue());
        user.updateEntity();
        BinaryContent binaryContent = BinaryContent.builder()
                .binaryContentUsage(BinaryContentUsage.PROFILE)
                .binaryFile(profileUpdateRequestDto.getFile())
                .build();

        userRepository.updateUser(user);
        binaryContentRepository.deleteBinaryContent(
                binaryContentRepository.readAllBinaryContent().stream().filter(x->
                        x.getId() == user.getProfileId()).findFirst().orElseThrow(()->new IllegalArgumentException("Profile not found"))
                        .getId()
        );
        binaryContentRepository.createBinaryContent(binaryContent);

    }

    @Override
    public List<User> readUpdatedUser() {
        return userRepository.getUpdatedUser();
    }

//    @Override
//    public void readDeletedUser() {
//        if (userRepository.getDeletedUser().length == 0) {
//            System.out.println("No Deleted User");
//            return;
//        }
//        System.out.println("===Deleted User=== ");
//        for (DeletedUserDto userDto : userRepository.getDeletedUser()) {
//            System.out.println(userDto.toString());
//        }
//        System.out.println("================");
//
//    }

    @Override
    public void enterChannel(UUID userId, UUID channelId) {
        User user = userRepository.getUserById(userId).orElseThrow(() -> new IllegalArgumentException(USER_NOT_EXIST));
        Channel channel = channelRepository.getChannelById(channelId).orElseThrow(() -> new IllegalArgumentException(USER_NOT_EXIST));
        user.addChannel(channelId);
        channel.addUserToChannel(userId);
        userRepository.updateUser(user);
        channelRepository.updateChannel(channel);

    }

    @Override
    public void exitChannel(UUID userId, UUID channelId) {
        User user = userRepository.getUserById(userId).orElseThrow(() -> new IllegalArgumentException(USER_NOT_EXIST));
        Channel channel = channelRepository.getChannelById(channelId).orElseThrow(() -> new IllegalArgumentException(USER_NOT_EXIST));
        user.removeChannel(channelId);
        channel.removeUserFromChannel(userId);
        userRepository.updateUser(user);
        channelRepository.updateChannel(channel);
    }
    private void userNameEmailCheck(User user){
        boolean isUserNameExit = userRepository.getAllUser().stream().anyMatch(x -> x.getUserName().equals(user.getUserName()));
        boolean isEmailExit = userRepository.getAllUser().stream().anyMatch(x -> x.getEmail().equals(user.getEmail()));
        if(isUserNameExit) throw new IllegalArgumentException("USERNAME_ALREADY_EXIST");
        if(isEmailExit) throw new IllegalArgumentException("EMAIL_ALREADY_EXIST");
    }

}
