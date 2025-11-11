package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.dto.request.channel.ChannelPatchRequestDto;
import com.sprint.mission.discodeit.dto.request.channel.ChannelPrivateCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.channel.ChannelPublicCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.ChannelReadResponseDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.entityElement.ChannelElement;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.sprint.mission.discodeit.service.util.StaticString.USER_NOT_EXIST;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    @Override
    public List<ChannelReadResponseDto> readAllChannel() {
        channelRepository.getAllChannel().forEach(x-> System.out.println(x.getName()+": "+x.getId()));
        return channelRepository.getAllChannel().stream().map(x->readChannel(x.getId())).toList();
    }

    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    @Override
    public ChannelReadResponseDto createPrivateChannel(ChannelPrivateCreateRequestDto channelPrivateCreateRequestDto) {

        List<User> userList = userRepository.getAllUser();
        HashSet<UUID> userIdList = channelPrivateCreateRequestDto.getParticipantIds();

    Channel channel = Channel.builder()
            .name(channelPrivateCreateRequestDto.getName())
            .isTextChannel(channelPrivateCreateRequestDto.isTextChannel())
            .isPublic(false)
            .joinUserList(userIdList)
            .description(channelPrivateCreateRequestDto.getDescription())
            .build();
    userList.forEach(x->x.addChannel(channel.getId()));

       channelPrivateCreateRequestDto.getParticipantIds().stream().map(
                x ->
                {
                    User tempUser = userRepository.getUserById(x).orElseThrow(()->new IllegalArgumentException(USER_NOT_EXIST));
                    tempUser.addChannel(channel.getId());
                    userRepository.updateUser(tempUser);
                    return ReadStatus.builder().userId(x).channelId(channel.getId()).build();
                }
        ).forEach(readStatusRepository::createReadStatus);
        Channel saveChannel = channelRepository.saveChannel(channel);

        return ChannelReadResponseDto.from(saveChannel);

    }

    @Override
    public ChannelReadResponseDto createPublicChannel(ChannelPublicCreateRequestDto channelPublicCreateRequestDto) {
    Channel channel = channelRepository.saveChannel(Channel.builder()
            .name(channelPublicCreateRequestDto.getName())
            .isTextChannel(channelPublicCreateRequestDto.isTextChannel)
            .isPublic(true)
            .description(channelPublicCreateRequestDto.getDescription())
            .joinUserList(channelPublicCreateRequestDto.getParticipantIds()==null
                    ? new HashSet<>()
                    : channelPublicCreateRequestDto.getParticipantIds())
            .build());

    return ChannelReadResponseDto.from(channel);


    }




    @Override
    public void createChannel(Channel channel) {
        channelRepository.saveChannel(channel);

    }

    @Override
    public ChannelReadResponseDto readChannel(UUID channelId) {
        Channel expectedChannel = channelRepository.getChannelById(channelId).orElseThrow(()->new IllegalArgumentException("Channel not found"));
        Instant max = lastPostTime(expectedChannel);
        ChannelReadResponseDto channelReadResponseDto = ChannelReadResponseDto.from(expectedChannel);
        return channelReadResponseDto;
    }

    private Instant lastPostTime(Channel channel){
        List<Message> messageList = messageRepository.getAllMessage();
        if(messageList.stream().noneMatch(x->x.getChannelId().equals(channel.getId()))) return Instant.MIN;

        return messageList.stream().
                filter(x -> x.getChannelId().equals(channel.getId()))
                .map(Entity::getUpdatedAt)
                .max(Comparator.naturalOrder()).orElseThrow(()->new IllegalArgumentException("Message not found"));
    }

    @Override
    public List<ChannelReadResponseDto> findAllByUserId(UUID userId) {
        List<Channel> channelList = channelRepository.getAllChannel();

        List<Channel> publicChannelList = channelList.stream().filter(Channel::isPublic).collect(Collectors.toList());
        List<Channel> userContainPrivateChannel = channelList.stream().filter(
                x->x.getJoinUserList().stream().anyMatch(y->y.equals(userId))&&!x.isPublic()
        ).toList();
        return Stream.concat(publicChannelList.stream(),userContainPrivateChannel.stream())
                .map(ChannelReadResponseDto::from
                        ).toList();

    }

    @Override
    public void deleteChannel(UUID channelID) {
        Channel targetChannel = channelRepository.getChannelById(channelID).orElseThrow(()->new IllegalArgumentException("존재하지 않는 Channel 입니다."));
        List<User> userList = userRepository.getAllUser();
        List<Message> messageList = messageRepository.getAllMessage();
        List<ReadStatus> readStatusList = readStatusRepository.readAllReadStatus();

        userRepository.getAllUser().stream().filter(x->x.getJoinChannelList().contains(targetChannel.getId())).forEach(
                x-> {
                    x.removeChannel(channelID);
                    userRepository.updateUser(x);
                }
        );
        readStatusList.stream().filter(x->x.getChannelId().equals(channelID)).forEach(x->readStatusRepository.deleteReadStatus(x.getId()));
        messageList.stream().filter(x->x.getChannelId().equals(channelID)).forEach(messageRepository::deleteMessage);
       channelRepository.deleteChannel(targetChannel);


    }

    @Override
    public <T> void updateChannel(ChannelUpdateRequestDto<T> channelUpdateRequestDto) {
        Channel channel = channelRepository.getChannelById(channelUpdateRequestDto.getChannelId()).
                orElseThrow(()->new IllegalArgumentException("존재하지 않는 Channel 입니다."));
        if(!channel.isPublic()) throw new IllegalArgumentException("Private Channel는 수정할 수 없습니다.");
        ChannelElement channelElement = channelUpdateRequestDto.getType();

        BiConsumer<Channel ,T> biConsumer = (BiConsumer<Channel, T>) channelElement.setter;
        biConsumer.accept(channel, channelUpdateRequestDto.getUpdatedValue());
        channel.updateEntity();
        channelRepository.updateChannel(channel);

    }

    @Override
    public List<Channel> readUpdatedChannel() {

        return channelRepository.getUpdatedChannel();

    }
//
//    @Override
//    public void readDeletedChannel() {
//        if(channelRepository.getDeletedChannel().length == 0){
//            System.out.println("No deleted channel");
//            return;
//        }
//        System.out.println("===Deleted Channel=== ");
//        for(DeletedChannelDto deletedChannelDto : channelRepository.getDeletedChannel()){
//            System.out.println(deletedChannelDto.toString());
//        }
//        System.out.println("=============");
//
//    }

    @Override
    public void inviteUserToChannel(UUID userId, UUID channelId) {

       User tempUser = userRepository.getUserById(userId).orElseThrow(()->new IllegalArgumentException(USER_NOT_EXIST));
       Channel tempChannel = channelRepository.getChannelById(channelId).orElseThrow(()->new IllegalArgumentException("Channel not found"));


        tempChannel.addUserToChannel(userId);
        tempUser.addChannel(channelId);

        channelRepository.updateChannel(tempChannel);
        userRepository.updateUser(tempUser);
        readStatusRepository.createReadStatus(ReadStatus.builder().userId(userId).channelId(channelId).build());

    }




    @Override
    public void deleteUserFromChannel(UUID userId, UUID channelId) {

        User tempUser = userRepository.getUserById(userId).orElseThrow(()->new IllegalArgumentException(USER_NOT_EXIST));
        Channel tempChannel = channelRepository.getChannelById(channelId).orElseThrow(()->new IllegalArgumentException("Channel not found"));
        var readStatusList = readStatusRepository.readAllReadStatus();

        tempChannel.removeUserFromChannel(userId);
        tempUser.removeChannel(channelId);

        channelRepository.updateChannel(tempChannel);
        userRepository.updateUser(tempUser);

        readStatusRepository.deleteReadStatus(readStatusList.stream()
                .filter(x->x.getUserId().equals(tempUser.getId())
                        && x.getChannelId().equals(tempChannel.getId()))
                .findFirst().orElseThrow().getId());

    }

    @Override
    public ChannelReadResponseDto patchChannel(ChannelPatchRequestDto dto, UUID channelId) {
        Channel channel = channelRepository.getChannelById(channelId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 Channel 입니다."));
        channel.setDescription(dto.newDescription());
        channel.setName(dto.newName());
        channel.updateEntity();
        channelRepository.updateChannel(channel);
        return ChannelReadResponseDto.from(channel);
    }

    @Override
    public void resetChannelRepository() {
        channelRepository.getAllChannel().forEach(x->deleteChannel(x.getId()));
    }
}
