package com.sprint.mission.discodeit.service.basic;

import static com.sprint.mission.discodeit.entity.ChannelType.PRIVATE;
import static com.sprint.mission.discodeit.entity.ChannelType.PUBLIC;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.dto.ChannelDto;
import com.sprint.mission.discodeit.entity.dto.ChannelDto_Update;
import com.sprint.mission.discodeit.entity.dto.Dto_CreateChannelPrivate;
import com.sprint.mission.discodeit.entity.dto.Dto_CreateChannelPublic;
import com.sprint.mission.discodeit.entity.dto.Res_Channel;
import com.sprint.mission.discodeit.entity.dto.Res_ChannelFind;
import com.sprint.mission.discodeit.repository.BaseInterfaceRepository;
import com.sprint.mission.discodeit.service.InterfaceChannelService;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor //!! final 필드나 @NonNull 어노테이션이 붙은 필드에 대한 생성자를 자동으로 생성
public class ChannelService implements InterfaceChannelService {
    private final BaseInterfaceRepository<Channel> channelRepository;
    private final BaseInterfaceRepository<Message> messageRepository;
    private final BaseInterfaceRepository<ReadStatus> readStatusRepository;

    @Override
    public Res_Channel createPublic(Dto_CreateChannelPublic dtoCreateChannel) {
        Channel channel = new Channel(dtoCreateChannel);
        channelRepository.save(channel);

        log.info("✅ createPublic = [" + dtoCreateChannel.name() + "] 채널 생성");

        return Res_Channel.from(channel);
    }

    @Override
    public Res_Channel createPrivate(Dto_CreateChannelPrivate dtoCreateChannel) {
//        PRIVATE 채널과 PUBLIC 채널을 생성하는 메소드를 분리합니다.
//        [ ] 분리된 각각의 메소드를 DTO를 활용해 파라미터를 그룹화합니다.
//                        PRIVATE 채널을 생성할 때:
//        [ ] 채널에 참여하는 User의 정보를 받아 User 별 ReadStatus 정보를 생성합니다.
//        [ ] name과 newDescription 속성은 생략합니다.
//        PUBLIC 채널을 생성할 때에는 기존 로직을 유지합니다.
        Channel channel = new Channel(dtoCreateChannel);
        channelRepository.save(channel);

        List<ReadStatus> list = dtoCreateChannel.participantIds().stream()
            .map(userId -> new ReadStatus(userId, channel.getId()))
            .peek(readStatus -> readStatusRepository.save(readStatus))
            .toList();

        return Res_Channel.from(channel);
    }

    @Override
    public Res_ChannelFind find(ChannelDto dtoChannel) {
//        [ ] 해당 채널의 가장 최근 메시지의 시간 정보를 포함합니다.
//        [ ] PRIVATE 채널인 경우 참여한 User의 readStatusID 정보를 포함합니다.

        Channel channel = channelRepository.findById(dtoChannel.id())
                .orElseThrow(() -> new NoSuchElementException("🚨 채널을 찾을 수 없음 :: " + dtoChannel.toString()));

        //!! 해당 채널의 가장 최근 메시지의 시간 정보
        List<Message> allMessageInChannel = messageRepository.findAllMessageInChannel(dtoChannel.id())
            .orElseThrow(() -> new IllegalArgumentException("🚨채널[" + dtoChannel.id().toString() + "]에 해당하는 메세지 없음"));
        Message message = allMessageInChannel.stream()
            .max(Comparator.comparing(Message::getCreatedAt))
            .orElse(null);
        Instant lastMessageAt = (message != null) ? message.getCreatedAt() : null;

        if (dtoChannel.channelType() == PRIVATE) {
            List<ReadStatus> readStatuses = readStatusRepository.findAll();
            List<UUID> userIdList = readStatuses.stream()
                .map(readStatus -> readStatus.getUserId())
                .distinct()
                .peek(userId -> log.info("✅ ChannelService.find <PRIVATE> 🔰.userids = [" + userId.toString() + "]"))
                .toList();

            return Res_ChannelFind.from(dtoChannel, lastMessageAt, userIdList);
        }
        else {
            log.info("✅ ChannelService.find <PUBLIC> = [" + channel.getChannelName() + "]");
            return Res_ChannelFind.from(dtoChannel, lastMessageAt, null);
        }
    }

    @Override
    public List<Res_ChannelFind> findAllByUserId(UUID userID) {
//        [ ] 해당 채널의 가장 최근 메시지의 시간 정보를 포함합니다.
//        [ ] PRIVATE 채널인 경우 참여한 User의 readStatusID 정보를 포함합니다.
//        [ ] 특정 User가 볼 수 있는 Channel 목록을(readStatus에는 Private 채널만 있음) 조회하도록 조회 조건을 추가하고, 메소드 명을 변경합니다. findAllByUserId

//        [ ] PUBLIC 채널 목록은 전체 조회합니다.
//        [ ] PRIVATE 채널은 조회한 User가 참여한 채널만 조회합니다.

        List<Res_ChannelFind> resChannelFinds = new ArrayList<>();

        List<Channel> allChannel = channelRepository.findAll();

        allChannel.stream()
            .filter(channel -> channel.getChannelType() == PUBLIC)
            .peek(channel -> log.info("✅ findAllByUserId.[✅ PUBLIC] = [" + channel.getChannelName() + "]"))
            .forEach(channel -> resChannelFinds.add(this.find(ChannelDto.create(channel))));

        //[ ] 특정 User가 볼 수 있는 Channel 목록 조회
        //[ ] PRIVATE 채널인 경우 참여한 User의 readStatusID 정보를 포함합니다.
        List<Channel> privateChannels = allChannel.stream()
            .filter(channel -> channel.getChannelType() == PRIVATE)
            .toList();

        List<ReadStatus> readStatusInUserID = readStatusRepository.findAll().stream()
            .filter(readStatus -> readStatus.getUserId().equals(userID))
            .toList();

        privateChannels.stream()
            .filter(channel -> readStatusInUserID.stream().anyMatch(readStatus -> readStatus.getChannelId().equals(channel.getId())))
            .peek(channel -> log.info("✅ findAllByUserId.[🅰️ PRIVATE]. userID = [" + userID.toString() + "]"))
            .forEach(channel -> resChannelFinds.add(this.find(ChannelDto.create(channel))));

        return resChannelFinds;
    }

    @Override
    public Res_Channel update(UUID channelId, ChannelDto_Update channelDtoUpdate) {
        Channel channel = channelRepository.findById(channelId)
            .orElseThrow(() -> new NoSuchElementException("🚨Channel[" + channelId.toString() + "]을 찾을 수 없음"));

        if (channel.getChannelType() == PRIVATE) {
            throw new IllegalArgumentException("🚨Private Channel은 수정할 수 없음");
        }
        else {
            Channel findedChannel = channelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("🚨update 오류"));

            findedChannel.update(channelDtoUpdate);
            channelRepository.save(findedChannel);

            log.info("✅ ChannelService.update = [" + channelDtoUpdate.newName() + "] [" + channelDtoUpdate.newDescription() + "]");

            return Res_Channel.from(findedChannel);
        }
    }

    @Override
    public void delete(UUID channelID) {
//        [ ] 관련된 도메인도 같이 삭제합니다.
//        Message, ReadStatus
        Channel findedChannel = channelRepository.findById(channelID)
            .orElseThrow(() -> new NoSuchElementException("Channel[" + channelID.toString() + "]을 찾을 수 없음"));

        channelRepository.deleteById(findedChannel.getId());

        readStatusRepository.findAll().stream()
          .filter(ReadStatus -> ReadStatus.getChannelId() == channelID)
          .forEach(readStatus -> readStatusRepository.deleteById(readStatus.getId()));

        messageRepository.findAll().stream()
          .filter(Message -> Message.getChannelId() == channelID)
          .forEach(message -> messageRepository.deleteById(message.getId()));

        log.info("✅ ChannelService.delete = [" + findedChannel.getChannelName() + "] 채널 삭제");
    }
}
