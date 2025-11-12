package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.dto.request.binaryContent.ProfileUpdateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserPatchRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.dto.request.userStatus.UserStatusPatchRequestDto;
import com.sprint.mission.discodeit.dto.response.UserCreateResponseDto;
import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.dto.response.UserReadResponseDto;
import com.sprint.mission.discodeit.dto.response.UserUserStatusPatchResponseDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.entityElement.BinaryContentUsage;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

import static com.sprint.mission.discodeit.service.util.StaticString.*;
import static com.sprint.mission.discodeit.service.util.StaticString.USER_NOT_EXIST;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;
//    private final HashMap<String, UserElement> userElementHashMap = new HashMap<>(){
//        {
//            put("NAME", UserElement.NAME);
//            put("NICKNAME", UserElement.NICKNAME);
//            put("EMAIL", UserElement.EMAIL);
//            put("ONLINE", UserElement.ONLINE);
//        }
//    }

//    @Override
//    public UserCreateResponseDto createUser(UserCreateRequestDto userCreateRequestDto) {
//        User user = User.builder()
//                .userName(userCreateRequestDto.getUserName())
//                .email(userCreateRequestDto.getEmail())
//                .password(userCreateRequestDto.getPassword())
//                .name(userCreateRequestDto.getUserName())
//                .build();
//        UserStatus userStatus = UserStatus.builder()
//                .userId(user.getId())
//                .lastOnlineTime(Instant.now())
//                .build();
//
//        userNameEmailCheck(user);
//
//        userStatusRepository.createUserStatus(userStatus);
//        userRepository.saveUser(user);
//
//        return userCreateResponseDto;
//    }

    @Override
    public UserCreateResponseDto createUser(UserCreateRequestDto userCreateRequestDto, MultipartFile profile) throws IOException {
        userNameEmailCheck(userCreateRequestDto);
    if(profile!=null) {
        BinaryContent binaryContent = binaryContentRepository.createBinaryContent(BinaryContent.builder()
                .fileName(profile.getName())
                .contentType(profile.getContentType())
                .size(profile.getSize())
                .bytes(profile.getBytes())
                .build()
        );

        User user = userRepository.saveUser(User.builder()
                .userName(userCreateRequestDto.getUsername())
                .name(userCreateRequestDto.getUsername())
                .email(userCreateRequestDto.getEmail())
                .profileId(binaryContent.getId())
                .password(userCreateRequestDto.getPassword())
                .build());

        userStatusRepository.createUserStatus(UserStatus.builder()
                .userId(user.getId())
                .lastOnlineTime(Instant.now())
                .build());

        return UserCreateResponseDto.builder()
                .id(user.getId())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .username(user.getUserName())
                .email(user.getEmail())
                .password(user.getPassword())
                .profileId(user.getProfileId())
                .build();
    }
        User user = userRepository.saveUser(User.builder()
                .userName(userCreateRequestDto.getUsername())
                .name(userCreateRequestDto.getUsername())
                .email(userCreateRequestDto.getEmail())
                .password(userCreateRequestDto.getPassword())
                .build());

        userStatusRepository.createUserStatus(UserStatus.builder()
                .userId(user.getId())
                .lastOnlineTime(Instant.now())
                .build());

        return UserCreateResponseDto.builder()
                .id(user.getId())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .username(user.getUserName())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
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
        userRepository.getAllUser().forEach(x-> System.out.println(x.getName()+": "+x.getId()));
        return userRepository.getAllUser().stream().map(x ->
                readUser(x.getId())).toList();
    }

    @Override
    public void deleteUser(UUID userId) {

        List<Channel> channelList = channelRepository.getAllChannel();
        List<Message> messageList = messageRepository.getAllMessage();
        List<UserStatus> userStatusList = userStatusRepository.readAllUserStatus();
        User targetUser = userRepository.getUserById(userId).orElseThrow(() -> new IllegalArgumentException(USER_NOT_EXIST));
        if (binaryContentRepository.isBinaryContentExist(targetUser.getProfileId())) {
            binaryContentRepository.deleteBinaryContent(targetUser.getProfileId());
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
                .bytes(profileUpdateRequestDto.getFile())
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

    private void userNameEmailCheck(UserCreateRequestDto dto){
        boolean isUserNameExit = userRepository.getAllUser().stream().anyMatch(x -> x.getUserName().equals(dto.getUsername()));
        boolean isEmailExit = userRepository.getAllUser().stream().anyMatch(x -> x.getEmail().equals(dto.getEmail()));
        if(isUserNameExit) throw new IllegalArgumentException("USERNAME_ALREADY_EXIST");
        if(isEmailExit) throw new IllegalArgumentException("EMAIL_ALREADY_EXIST");
    }

    @Override
    public void updateUserOnlineStatus(UUID userId) {
        User targetUser = userRepository.getUserById(userId).orElseThrow(() -> new IllegalArgumentException(USER_NOT_EXIST));
        UserStatus targetUserStatus = userStatusRepository.readAllUserStatus().stream().filter(x -> x.getUserId().equals(userId)).findFirst().orElseThrow(() -> new IllegalArgumentException(USER_NOT_EXIST));
        targetUser.setOnline(targetUserStatus.isUserOnline());
        userRepository.updateUser(targetUser);
    }

    @Override
    public void resetUserRepository() {
        userRepository.resetUserRepository();
    }

    @Override
    public List<UserDto> advanceFindAllUser(){
        return userRepository.getAllUser().stream().map(
        x-> UserDto.builder()
                .id(x.getId())
                .createdAt(x.getCreatedAt())
                .updatedAt(x.getUpdatedAt())
                .username(x.getUserName())
                .email(x.getEmail())
                .profileId(x.getProfileId() == null? null: x.getProfileId())
                .online(x.isOnline())
                .build()
                ).toList();
    }

    @Override
    public UserCreateResponseDto patchUser(UUID userId, UserUpdateRequest dto, MultipartFile profile) throws IOException {
        User user = userRepository.getUserById(userId).orElseThrow(() -> new IllegalArgumentException(USER_NOT_EXIST));
        user.setUserName(dto.newUsername());
        user.setName(dto.newUsername());
        user.setPassword(dto.newPassword());
        user.setEmail(dto.newEmail());
        if(profile!=null) {
            binaryContentRepository.deleteBinaryContent(user.getProfileId());
            BinaryContent tmpBinaryContent = binaryContentRepository.createBinaryContent(BinaryContent.builder()
                    .bytes(profile.getBytes())
                    .fileName(profile.getName())
                    .size(profile.getSize())
                    .contentType(profile.getContentType())
                    .build());
            user.setProfileId(tmpBinaryContent.getId());
        }
        user.updateEntity();
        userRepository.updateUser(user);

        return UserCreateResponseDto.builder()
                .id(user.getId())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .email(user.getEmail())
                .username(user.getUserName())
                .password(user.getPassword())
                .profileId(user.getProfileId()!=null?user.getProfileId():null)
                .build();
    }


}
