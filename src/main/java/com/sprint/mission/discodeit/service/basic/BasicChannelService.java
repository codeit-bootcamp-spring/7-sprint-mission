package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.request.CreateChannelRequestDto;
import com.sprint.mission.discodeit.dto.channel.request.UpdateChannelRequestDto;
import com.sprint.mission.discodeit.dto.channel.request.UpdateChannelNameRequestDto;
import com.sprint.mission.discodeit.dto.channel.response.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.response.PrivateChannelResponseDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;

    @Override
    public ChannelResponseDto createPublic(CreateChannelRequestDto request) {
        // 이름 중복 저장 불가
        if(channelRepository.existsByName(request.getChannelName())) {
            throw new IllegalArgumentException("채널 이름이 존재합니다. 다시 입력해주세요.");
        }

        Channel newChannel = new Channel(
                request.getChannelType(),
                ChannelVisibility.PUBLIC,
                request.getChannelName(),
                request.getAdminId());

        channelRepository.save(newChannel);

        // 최근 메시지가 없으므로 채널 생성 시간으로 저장
        return ChannelResponseDto.from(newChannel, newChannel.getCreatedAt());
    }

    @Override
    public PrivateChannelResponseDto createPrivate(CreateChannelRequestDto request) {
        // 이름 중복 저장 불가
        if(channelRepository.existsByName(request.getChannelName())) {
            throw new IllegalArgumentException("채널 이름이 존재합니다. 다시 입력해주세요.");
        }

        Channel newChannel = new Channel(
                request.getChannelType(),
                ChannelVisibility.PRIVATE,
                request.getChannelName(),
                request.getAdminId());

        channelRepository.addChannelIdForUser(newChannel.getId(), request.getAdminId()); // 유저 객체에 속한 채널 UUID 리스트 저장
        channelRepository.save(newChannel);
        readStatusRepository.save(new ReadStatus(request.getAdminId(), newChannel.getId()));
        return PrivateChannelResponseDto.from(newChannel, newChannel.getCreatedAt());
    }

    @Override
    public void addMember(UpdateChannelRequestDto request){
        Channel channel = channelRepository.findById(request.getChannelId())
                .orElseThrow(() -> new IllegalArgumentException("채널이 존재하지 않습니다."));

        if(channel.getMemberIds().contains(request.getUserid())){
            throw new IllegalArgumentException("이미 멤버가 채널에 속해있습니다.");
        }

        if(channel.getVisibility() == ChannelVisibility.PUBLIC){
            throw new IllegalArgumentException("공개 채널은 멤버를 추가할 수 없습니다.");
        }

        readStatusRepository.save(new ReadStatus(request.getUserid(), request.getChannelId()));
        channel.addMember(request.getUserid());
        channelRepository.addChannelIdForUser(channel.getId(), request.getUserid()); // 유저 객체에 속한 채널 UUID 리스트 저장
        channelRepository.save(channel);
    }

    @Override
    public ChannelResponseDto find(UUID channelId){
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("채널이 존재하지 않습니다."));
        Instant lastedMessageAt = messageRepository.searchLastedMessageTime(channelId);
        return channel.getVisibility() == ChannelVisibility.PRIVATE
                ? PrivateChannelResponseDto.from(channel, lastedMessageAt)
                : ChannelResponseDto.from(channel, lastedMessageAt);
    }

    // 유저가 속한 Private 채널만 조회
    @Override
    public List<ChannelResponseDto> findPrivateByUserId(UUID userId) {
        return channelRepository.findByUser(userId).stream()
                .map(c ->
                        (ChannelResponseDto) PrivateChannelResponseDto.from(c, messageRepository.searchLastedMessageTime(c.getId()))
                )
                .sorted(Comparator.comparing(ChannelResponseDto::getChannelType)
                        .thenComparing(c -> c.getChannelName())
                )
                .collect(Collectors.toList());
    }

    @Override
    public List<ChannelResponseDto> findByType(ChannelType type) {
        return channelRepository.findByType(type).stream()
                .map(c -> c.getVisibility() == ChannelVisibility.PRIVATE
                                ? PrivateChannelResponseDto.from(c, messageRepository.searchLastedMessageTime(c.getId()))
                                : ChannelResponseDto.from(c, messageRepository.searchLastedMessageTime(c.getId()))
                )
                .sorted(Comparator.comparing(c -> c.getChannelName()))
                .collect(Collectors.toList());
    }

    // Public 채널 목록은 전체 조회 + Private 채널은 User가 참여한 채널만 조회
    @Override
    public List<ChannelResponseDto> findAllByUserId(UUID userId) {
        return channelRepository.findAll().stream()
                .filter(c -> c.getVisibility() == ChannelVisibility.PUBLIC ||
                        (c.getVisibility() == ChannelVisibility.PRIVATE && c.getMemberIds().contains(userId)))
                .map(c -> c.getVisibility() == ChannelVisibility.PRIVATE
                                ? PrivateChannelResponseDto.from(c, messageRepository.searchLastedMessageTime(c.getId()))
                                : ChannelResponseDto.from(c, messageRepository.searchLastedMessageTime(c.getId()))
                )
                .sorted(Comparator.comparing(ChannelResponseDto::getVisibility)
                        .thenComparing(c -> c.getChannelType())
                        .thenComparing(c -> c.getChannelName())
                )
                .collect(Collectors.toList());
    }

    @Override
    public void updateAdmin(UpdateChannelRequestDto request) {
        Channel channel = channelRepository.findById(request.getChannelId())
                .orElseThrow(() -> new IllegalArgumentException("채널이 존재하지 않습니다."));

        if (!channel.getMemberIds().contains(request.getUserid())) {
            throw new IllegalArgumentException("그 유저는 이 채널에 속해 있지 않아 관리자로 변경할 수 없습니다.");
        } else if (channel.getAdminId().equals(request.getUserid())) {
            throw new IllegalArgumentException("그 유저는 이미 이 채널의 관리자 입니다.");
        }

        channel.setAdmin(request.getUserid());
        channelRepository.update(channel);
    }

    @Override
    public void updateName(UpdateChannelNameRequestDto request) {
        Channel channel = channelRepository.findById(request.getChannelId())
                .orElseThrow(() -> new IllegalArgumentException("채널이 존재하지 않습니다."));

        if (channelRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("채널 이름이 존재합니다. 다시 입력해주세요.");
        }

        channel.setChannelName(request.getName());
        channelRepository.update(channel);
    }

    @Override
    public void delete(UUID channelId, UUID userId) {
        if(!userRepository.isExist(userId)) {
            throw new IllegalArgumentException("유저가 존재하지 않습니다.");
        }
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("채널이 존재하지 않습니다."));

        if(!userId.equals(channel.getAdminId())) {
            throw new IllegalArgumentException("관리자가 아니므로 채널을 삭제할 수 없습니다.");
        }

        readStatusRepository.deleteByChannelId(channelId);
        messageRepository.deleteByChannelId(channelId);
        channelRepository.deleteById(channelId); // 채널 삭제
    }

    @Override
    public void deleteChannelMember(UUID channelId, UUID requesterId, UUID targetId) {
        if(!userRepository.isExist(targetId)) {
            throw new IllegalArgumentException("유저가 존재하지 않습니다.");
        }
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("채널이 존재하지 않습니다."));

        //삭제 요청 유저와 삭제될 유저가 동일하지 않으면
        if(!requesterId.equals(targetId)){
            if(!requesterId.equals(channel.getAdminId())) { //삭제 요청 유저가 관리자가 아니라면 삭제 거부
                throw new IllegalArgumentException("관리자가 아니므로 다른 유저를 채널에서 삭제할 수 없습니다.");
            } else if(!channel.getMemberIds().contains(targetId)) {
                throw new IllegalArgumentException("삭제하려는 유저가 채널에 속해 있지 않습니다.");
            }
        } else if(channel.getAdminId().equals(targetId)) {
            throw new IllegalArgumentException("당신은 관리자이므로 채널을 나갈 수 없습니다.");
        }

        // 특정 유저가 나가도 메시지는 채널에 유지
        readStatusRepository.deleteByChannelMember(channelId, targetId);
        channelRepository.deleteMember(channel, targetId);
    }

    @Override
    public boolean isChannelUnavailableForUser(UUID userId, UUID channelId) {
        // 채널이 존재하거나 유저가 채널에 속해있다면 false를 반환
        if(channelRepository.existsById(channelId) || channelRepository.isUserJoinedChannel(userId, channelId)) {
            return false;
        }

        return true;
    }


}
