package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.Util;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.dto.*;
import com.sprint.mission.discodeit.repository.InterfaceChannelRepository;
import com.sprint.mission.discodeit.repository.InterfaceMessageRepository;
import com.sprint.mission.discodeit.repository.InterfaceReadStatusRepository;
import com.sprint.mission.discodeit.service.InterfaceChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

import static com.sprint.mission.discodeit.entity.ChannelType.PRIVATE;
import static com.sprint.mission.discodeit.entity.ChannelType.PUBLIC;

@Service
@RequiredArgsConstructor //!! final 필드나 @NonNull 어노테이션이 붙은 필드에 대한 생성자를 자동으로 생성
public class ChannelService implements InterfaceChannelService {
    private final InterfaceChannelRepository channelRepository;
    private final InterfaceReadStatusRepository readStatusRepository;
    private final InterfaceMessageRepository messageRepository;


    @Override
    public Res_Channel createPublic(Dto_CreateChannelPublic dtoCreateChannel) {
        Channel channel = new Channel(dtoCreateChannel);
        channelRepository.save(channel);
        Util.okMessage("createPublic = [" + dtoCreateChannel.channelName() + "] 채널 생성");
        return Res_Channel.from(channel);
    }

    @Override
    public Res_Channel createPrivate(Dto_CreateChannelPrivate dtoCreateChannel) {
//        PRIVATE 채널과 PUBLIC 채널을 생성하는 메소드를 분리합니다.
//        [ ] 분리된 각각의 메소드를 DTO를 활용해 파라미터를 그룹화합니다.
//                        PRIVATE 채널을 생성할 때:
//        [ ] 채널에 참여하는 User의 정보를 받아 User 별 ReadStatus 정보를 생성합니다.
//        [ ] name과 description 속성은 생략합니다.
//        PUBLIC 채널을 생성할 때에는 기존 로직을 유지합니다.
        Channel channel = new Channel(dtoCreateChannel);
        channelRepository.save(channel);

        List<ReadStatus> list = dtoCreateChannel.userIDs().stream().map(userId -> new ReadStatus(userId, channel.getId())).toList();
        list.forEach(readStatusRepository::save);

        for (UUID uuid : dtoCreateChannel.userIDs().stream().toList()) {
            Util.okMessage("PrivateChannel.userIDs = [" + uuid + "]");
        }

        return Res_Channel.from(channel);
    }

    @Override
    public Res_ChannelFind find(Dto_Channel dtoChannel) {
//        [ ] 해당 채널의 가장 최근 메시지의 시간 정보를 포함합니다.
//        [ ] PRIVATE 채널인 경우 참여한 User의 id 정보를 포함합니다.

        Channel channel = channelRepository.findById(dtoChannel.id())
                .orElseThrow(() -> new NoSuchElementException("채널을 찾을 수 없습니다."));

        //!! 해당 채널의 가장 최근 메시지의 시간 정보
        List<Message> allMessageInChannel = messageRepository.findAllMessageInChannel(dtoChannel.id()).orElseThrow(() -> new IllegalArgumentException("🚨channel.find = 채널에 해당하는 메세지 없음"));
        Message message = allMessageInChannel.stream().max(Comparator.comparing(Message::getCreatedAt)).orElse(null);
        Instant lastMessageAt = (message != null) ? message.getCreatedAt() : null;

        //!!⭐️ ReadStatus = Private Channel 만 가능??
        //!! PRIVATE 채널인 경우 참여한 User의 id 정보
        if (dtoChannel.channelType() == PRIVATE) {
            List<ReadStatus> readStatuses = readStatusRepository.findAll().orElseThrow(() -> new NullPointerException("hannel.find.readStatusRepository.findAll() 에러"));
            List<UUID> userIdList = readStatuses.stream().map(readStatus -> readStatus.getUserId()).distinct().toList();

            userIdList.forEach(userId -> Util.okMessage("ChannelService.find <PRIVATE> 🔰.userids = [" + userId + "]"));
            return Res_ChannelFind.from(dtoChannel, lastMessageAt, userIdList);
        }
        else {
            Util.okMessage("ChannelService.find <PUBLIC> = [" + channel.getChannelName() + "]");
            return Res_ChannelFind.from(dtoChannel, lastMessageAt, null);
        }
    }

    @Override
    public List<Res_ChannelFind> findAllByUserId(UUID userID) {
//        [ ] 해당 채널의 가장 최근 메시지의 시간 정보를 포함합니다.
//        [ ] PRIVATE 채널인 경우 참여한 User의 id 정보를 포함합니다.
//        [ ] 특정 User가 볼 수 있는 Channel 목록을(readStatus에는 Private 채널만 있음) 조회하도록 조회 조건을 추가하고, 메소드 명을 변경합니다. findAllByUserId

//        [ ] PUBLIC 채널 목록은 전체 조회합니다.
//        [ ] PRIVATE 채널은 조회한 User가 참여한 채널만 조회합니다.

        List<Res_ChannelFind> resChannelFinds = new ArrayList<>();

        List<Channel> allChannel = channelRepository.findAll().orElseThrow(() -> new NoSuchElementException("🚨findallByChannleId.channelRepository.findAll() 오류"));
        List<Channel> publicChannels = allChannel.stream().filter(channel -> channel.getChannelType() == PUBLIC).toList();

        for (Channel publicChannel : publicChannels) {
            resChannelFinds.add(this.find(Dto_Channel.from(publicChannel)));
            Util.okMessage("ChannelService.findAllByUserId.[PUBLIC].channelName = [" + publicChannel.getChannelName() + "]");
        }

        //!!⭐️ ReadStatus = Private Channel 만 가능??
        //[ ] 특정 User가 볼 수 있는 Channel 목록 조회
        List<ReadStatus> readStatuses = readStatusRepository.findAll().orElseThrow(() -> new NullPointerException("🚨findallByChannleId.readStatusRepository.findAl() 오류"));
        List<ReadStatus> readStatusWithUserID = readStatuses.stream().filter(readStatus -> userID.equals(readStatus.getUserId())).toList();
        List<UUID> privateChannelIdsInReadStatus = readStatusWithUserID.stream().map(readStatus -> readStatus.getChannelId()).toList(); // distinct().
        List<Channel> privateChannels = allChannel.stream().filter(channel -> privateChannelIdsInReadStatus.contains(channel.getId())).toList();

        for (Channel privateChannel : privateChannels) {
            resChannelFinds.add(this.find(Dto_Channel.from(privateChannel)));
            Util.okMessage("ChannelService.findAllByUserId.[PRIVATE]");
        }

//        Util.okMessage("ChannelService.findAllByUserId.userID = [" + userID + "]");
//        for (Res_ChannelFind resChannelFind : resChannelFinds) {
//            if (resChannelFind.channelType() == PRIVATE) {
//                Util.okMessage("ChannelService.findAllByUserId.[PRIVATE].userID = [" + resChannelFind.privateChannelUserIDs() + "]");
//            }
//            else {
//                Util.okMessage("ChannelService.findAllByUserId.[PUBLIC].channelName = [" + resChannelFind.channelName() + "]");
//            }
//        }

        return resChannelFinds;
    }

//    @Override
//    public void updateChannelName(Channel channel, String reName) {
//        String message = "updateChannelName = 채널이름을 [" + channel.getChannelName() + "] 에서 [" + reName + "] 로 변경";
//        Channel findedChannel = channelRepository.findById(channel.getId()).orElseThrow(() -> new IllegalArgumentException("🚨updateChannelName. channel = [" + channel.getChannelName() + "] 없음 오류"));
//        if (!channelRepository.existsByName(reName)) {
//            Util.errMessage(message + " 오류");
//        }
//        else {
//            findedChannel.updateChannelName(reName);
//            channelRepository.save(findedChannel);
//            Util.okMessage(message);
//        }
//    }
//
//    @Override
//    public void updateChannelType(Channel channel, ChannelType channelType) {
//        Channel findedChannel = channelRepository.findById(channel.getId()).orElseThrow(() -> new IllegalArgumentException("🚨updateChannelType 오류"));
//        findedChannel.updateChannelType(channelType);
//        channelRepository.save(findedChannel);
//        Util.okMessage("updateChannelType = [" + channel.getChannelName() + "] 채널 타입이 [" + channelType.name() + "] 으로 변경");
//    }

    @Override
    public void update( Dto_ChannelUpdate dtoChannelUpdate) {
        Channel channel = channelRepository.findById(dtoChannelUpdate.channelID())
                .orElseThrow(() -> new IllegalArgumentException("ChannelService.update.dtoChannelUpdate.channelID = [" + dtoChannelUpdate.channelID()+ "] 오류"));


        //[ ] PRIVATE 채널은 수정할 수 없습니다.
        if (channel.getChannelType() == PRIVATE) {
            Util.errMessage("PRIVATE 채널 수정 불가");
        }
        else {
            Channel findedChannel = channelRepository.findById(dtoChannelUpdate.channelID()).orElseThrow(() -> new IllegalArgumentException("🚨update 오류"));
            findedChannel.update(dtoChannelUpdate);
            channelRepository.save(findedChannel);
            Util.okMessage("ChannelService.update = [" + dtoChannelUpdate.channelName() + "] [" + dtoChannelUpdate.description() + "]");
        }
    }

    @Override
    public void delete(UUID channelID) {
//        [ ] 관련된 도메인도 같이 삭제합니다.
//        Message, ReadStatus
        Channel findedChannel = channelRepository.findById(channelID).orElseThrow(() -> new IllegalArgumentException("🚨delete. id = [" + channelID.toString() + "] 오류"));
        channelRepository.deleteById(findedChannel.getId());

        List<ReadStatus> allResdStatus = readStatusRepository.findAll().orElseThrow(() -> new NoSuchElementException("🚨findallByChannleId() 오류"));
        List<ReadStatus> filteredReadStatuses = allResdStatus.stream().filter(ReadStatus -> ReadStatus.getChannelId() == channelID).toList();

        for (ReadStatus readStatus : filteredReadStatuses) {
            readStatusRepository.deleteById(readStatus.getId());
        }

        List<Message> allMessage = messageRepository.findAll().orElseThrow(() -> new NoSuchElementException("🚨findallByChannleId() 오류"));
        List<Message> messageList = allMessage.stream().filter(Message -> Message.getChannelId() == channelID).toList();

        for (Message message : messageList) {
            messageRepository.deleteById(message.getId());
        }

        Util.okMessage("ChannelService.delete = [" + findedChannel.getChannelName() + "] 채널 삭제");
    }
}
