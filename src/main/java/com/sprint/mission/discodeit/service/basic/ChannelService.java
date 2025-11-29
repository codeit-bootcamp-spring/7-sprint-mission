package com.sprint.mission.discodeit.service.basic;

import static com.sprint.mission.discodeit.entity.ChannelType.PRIVATE;
import static com.sprint.mission.discodeit.entity.ChannelType.PUBLIC;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.mapper.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.ChannelDto_Update;
import com.sprint.mission.discodeit.mapper.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.mapper.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.repository.jpa.ChannelsRepository;
import com.sprint.mission.discodeit.repository.jpa.MessagesRepository;
import com.sprint.mission.discodeit.repository.jpa.ReadStatusesRepository;
import com.sprint.mission.discodeit.repository.jpa.UsersRepository;
import com.sprint.mission.discodeit.service.InterfaceChannelService;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional // 영속성 컨텍스트
@RequiredArgsConstructor //!! final 필드나 @NonNull 어노테이션이 붙은 필드에 대한 생성자를 자동으로 생성
public class ChannelService implements InterfaceChannelService {
    private final ChannelsRepository channelRepository;
    private final MessagesRepository messageRepository;
    private final ReadStatusesRepository readStatusRepository;
    private final UsersRepository userRepository;
    private final ChannelMapper channelMapper;

    private List<ReadStatus> getReadStatusList( Channel channel, List<UUID> participantIds) {

        List<ReadStatus> readStatusList = new ArrayList<>();

        if (channel.getType() == PRIVATE) { // private 일때만 readStatus 생성 //?????🚨🚨🚨🚨
            readStatusList = participantIds.stream()
                .map(userId -> { User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("createPrivate::해당 user[" + userId.toString() + "] 없음"));
                    return new ReadStatus(user, channel, Instant.now());
                })
                .peek(readStatusRepository::save)
                .toList();
        }

        return readStatusList;
    }

    @Override
    public ChannelDto createPublic(PublicChannelCreateRequest dtoCreateChannel) {
        //log.info("🩷 Channel createPublic");

        Channel channel = new Channel(PUBLIC
                                    , dtoCreateChannel.name()
                                    , dtoCreateChannel.description());

        channelRepository.save(channel);

//        List<ReadStatus> list = getReadStatusList(channel, dtoCreateChannel.participantIds());

        log.info("✅ createPublic = [" + dtoCreateChannel.name() + "] 채널 생성");

        return channelMapper.toDto(channel);
    }

    @Override
    public ChannelDto createPrivate(PrivateChannelCreateRequest dtoCreateChannel) {
        //log.info("🩷 Channel createPrivate");
//        PRIVATE 채널과 PUBLIC 채널을 생성하는 메소드를 분리합니다.
//        [ ] 분리된 각각의 메소드를 DTO를 활용해 파라미터를 그룹화합니다.
//                        PRIVATE 채널을 생성할 때:
//        [ ] 채널에 참여하는 User의 정보를 받아 User 별 ReadStatus 정보를 생성합니다.
//        [ ] name과 newDescription 속성은 생략합니다.
//        PUBLIC 채널을 생성할 때에는 기존 로직을 유지합니다.
        Channel channel = new Channel(PRIVATE,
                                    null,
                                    null);

        channelRepository.save(channel);

//        List<ReadStatus> list = dtoCreateChannel.participantIds().stream()
//            .map(userId -> { User user = userRepository.findById(userId)
//                                            .orElseThrow(() -> new IllegalArgumentException("createPrivate::해당 user[" + userId.toString() + "] 없음"));
//                return new ReadStatus(user, channel, Instant.now());
//            })
//            .peek(readStatusRepository::save)
//            .toList();

        List<ReadStatus> list = getReadStatusList(channel, dtoCreateChannel.participantIds());

        log.info("✅ createPrivate 채널 생성");

        return channelMapper.toDto(channel);
    }

    @Override
    public List<ChannelDto> findAllByUserId(UUID userID) {
        //log.info("🩷 Channel findAllByUserId");
//        [ ] 해당 채널의 가장 최근 메시지의 시간 정보를 포함합니다.
//        [ ] PRIVATE 채널인 경우 참여한 User의 readStatusID 정보를 포함합니다.
//        [ ] 특정 User가 볼 수 있는 Channel 목록을 조회하도록 조회 조건을 추가하고, 메소드 명을 변경합니다. findAllByUserId
//        [ ] PUBLIC 채널 목록은 전체 조회합니다.
//        [ ] PRIVATE 채널은 조회한 User가 참여한 채널만 조회합니다.
        List<Channel> allChannel = channelRepository.findAll();
        List<ChannelDto> resChannelFinds = new ArrayList<>();

        allChannel.stream()
            .filter(channel -> channel.getType() == PUBLIC)
            .peek(channel -> log.info("✅ findAllByUserId.[✅ PUBLIC] = [" + channel.getName() + "]"))
            .forEach(channel -> resChannelFinds.add(channelMapper.toDto(channel)));

        allChannel.stream()
            .filter(channel -> channel.getType() == PRIVATE)
            .filter(channel -> readStatusRepository.findReadStatusByUserIdAndChannelId(userID, channel.getId()).isPresent())
            .peek(channel -> log.info("✅ findAllByUserId.[🅰️ PRIVATE]. userID = [" + userID.toString() + "]"))
            .forEach(channel -> resChannelFinds.add(channelMapper.toDto(channel)));

        return resChannelFinds;
    }

    @Override
    public ChannelDto update(UUID channelId, ChannelDto_Update channelDtoUpdate) {
        //log.info("🩷 Channel update");
        Channel channel = channelRepository
            .findById(channelId)
            .orElseThrow(() -> new NoSuchElementException("🚨Channel[" + channelId.toString() + "]을 찾을 수 없음"));

        if (channel.getType() == PRIVATE) {
            throw new IllegalArgumentException("🚨Private Channel은 수정할 수 없음");
        }
        else {
            Channel findedChannel = channelRepository
                .findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("🚨channelRepository.update 오류"));

            findedChannel.setName(channelDtoUpdate.newName());
            findedChannel.setDescription(channelDtoUpdate.newDescription());

            channelRepository.save(findedChannel);

            log.info("✅ ChannelService.update = [" + channelDtoUpdate.newName() + "] [" + channelDtoUpdate.newDescription() + "]");

            return channelMapper.toDto(findedChannel);
        }
    }

    @Override
    public void delete(UUID channelID) {
        //log.info("🩷 Channel delete");
//        [ ] 관련된 도메인도 같이 삭제합니다.
//        Message, ReadStatus
        Channel findedChannel = channelRepository.findById(channelID)
            .orElseThrow(() -> new NoSuchElementException("Channel[" + channelID.toString() + "]을 찾을 수 없음"));

        channelRepository.deleteById(findedChannel.getId());

        readStatusRepository.findAll().stream()
          .filter(readStatus -> readStatus.getChannel().getId().equals(channelID))
          .forEach(readStatus -> readStatusRepository.deleteById(readStatus.getId()));

        messageRepository.findAll().stream()
          .filter(message -> message.getChannel().getId().equals(channelID))
          .forEach(message -> messageRepository.deleteById(message.getId()));

        log.info("✅ ChannelService.delete = [" + findedChannel.getName() + "] 채널 삭제");
    }
}
