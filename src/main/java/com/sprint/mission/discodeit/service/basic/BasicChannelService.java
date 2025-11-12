package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.request.*;
import com.sprint.mission.discodeit.dto.channel.response.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.response.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.response.PrivateChannelResponseDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.global.exception.custom.CustomException;
import com.sprint.mission.discodeit.global.exception.custom.ErrorCode;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    // 기존의 채널 생성 메서드
    @Override
    public ChannelResponseDto create(UUID adminId, CreateChannelRequestDto request) {
        // 이름 중복 저장 불가
        if(existsByName(request.getChannelName())) {
            throw new CustomException(ErrorCode.CHANNEL_NAME_ALREADY_EXISTS);
        }

        Channel newChannel = new Channel(
                request.getChannelType(),
                request.getChannelVisibility(),
                request.getChannelName(),
                request.getDescription(),
                adminId);

        channelRepository.save(newChannel);

        // 비공개 채널의 경우 멤버 정보 저장
        if (request.getChannelVisibility() == ChannelVisibility.PRIVATE) {
            channelRepository.addChannelIdForUser(newChannel.getId(), adminId); // 유저 객체에 속한 채널 UUID 리스트 저장
            readStatusRepository.save(new ReadStatus(adminId, newChannel.getId(), Instant.MIN));
        }

        return ChannelResponseDto.from(newChannel, newChannel.getCreatedAt());
    }

    // API 스펙에 맞는 공개 채널 및 비공개 채널 생성 메서드 추가
    @Override
    public Channel create(CreatePublicChannelRequestDto request) {
        String name = request.getName();
        String description = request.getDescription();

        // 이름 중복 저장 불가
        if(existsByName(name)) {
            throw new CustomException(ErrorCode.CHANNEL_NAME_ALREADY_EXISTS);
        }

        Channel channel = new Channel(ChannelType.MESSAGE, ChannelVisibility.PUBLIC, name, description, null);
        channelRepository.save(channel);
        return channel;
    }

    @Override
    public Channel create(CreatePrivateChannelRequestDto request) {
        Channel channel = new Channel(ChannelType.MESSAGE, ChannelVisibility.PRIVATE, null, null, null);
        channelRepository.save(channel);
        request.getParticipantIds().stream()
                .forEach(userId ->
                        addMember(channel.getId(), new UpdateChannelRequestDto(userId))
                );
        return channel;
    }

    @Override
    public Channel update(UUID channelId, UpdatePublicChannelRequestDto request) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() ->  new CustomException(ErrorCode.CHANNEL_NOT_FOUND));

        // 이름 중복 저장 불가
        if(existsByName(request.getNewName())) {
            throw new CustomException(ErrorCode.CHANNEL_NAME_ALREADY_EXISTS);
        }
        if (channel.getVisibility() ==  ChannelVisibility.PRIVATE) {
            throw new CustomException(ErrorCode.PRIVATE_CHANNEL_UPDATE_FORBIDDEN);
        }
        channel.update(request.getNewName(), request.getNewDescription());
        channelRepository.update(channel);
        return channel;
    }


    @Override
    public void addMember(UUID channelId, UpdateChannelRequestDto request){
        userRepository.findById(request.getUserId())
                .orElseThrow(() -> new  CustomException(ErrorCode.USER_NOT_FOUND));

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHANNEL_NOT_FOUND));

        // 요청된 사용자가 이미 채널에 속해 있는 경우 예외 발생
        if(channel.getMemberIds().contains(request.getUserId())){
            throw new CustomException(ErrorCode.CHANNEL_MEMBER_ALREADY_EXISTS);
        }

        // 공개 채널의 경우 예외 발생 (비공개 채널만 멤버 추가 가능)
        if(channel.getVisibility() == ChannelVisibility.PUBLIC){
            throw new CustomException(ErrorCode.PUBLIC_CHANNEL_MEMBER_ADD_FORBIDDEN);
        }

        readStatusRepository.save(new ReadStatus(request.getUserId(), channelId, Instant.now()));
        channel.addMember(request.getUserId());
        channelRepository.addChannelIdForUser(channel.getId(), request.getUserId()); // 유저 객체에 속한 채널 UUID 리스트 저장
        channelRepository.save(channel);
    }

    @Override
    public ChannelResponseDto find(UUID channelId){
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHANNEL_NOT_FOUND));
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
    public List<ChannelDto> findAllByUserId(UUID userId) {
        return channelRepository.findAll().stream()
                .filter(c -> c.getVisibility() == ChannelVisibility.PUBLIC ||
                        (c.getVisibility() == ChannelVisibility.PRIVATE && c.getMemberIds().contains(userId)))
                .map(c -> ChannelDto.from(c, messageRepository.searchLastedMessageTime(c.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public void updateAdmin(UUID channelId, UpdateChannelAdminRequestDto request) {
        userRepository.findById(request.getNewAdminId())
                .orElseThrow(() -> new  CustomException(ErrorCode.NEW_ADMIN_USER_NOT_FOUND));

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHANNEL_NOT_FOUND));

        // 관리자만 채널의 관리자 변경 가능
        if(channel.getAdminId().equals(request.getAdminId())){
            if(channel.getVisibility() == ChannelVisibility.PRIVATE){
                // 새로운 관리자가 채널에 속하지 않은 경우 예외 발생
                if (!channel.getMemberIds().contains(request.getNewAdminId())) {
                    throw new CustomException(ErrorCode.NOT_CHANNEL_MEMBER);
                }
            // 관리자가 본인을 관리자를 변경하려 하는 경우 예외 발생
            } else if (channel.getAdminId().equals(request.getNewAdminId())) {
                throw new CustomException(ErrorCode.ALREADY_CHANNEL_ADMIN);
            }

            channel.setAdmin(request.getNewAdminId());
            channelRepository.update(channel);
        } else {
            throw new CustomException(ErrorCode.NOT_CHANNEL_ADMIN);
        }
    }

    @Override
    public void updateName(UUID channelId, UpdateChannelNameRequestDto request) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHANNEL_NOT_FOUND));

        // 관리자만 채널 이름 변경 가능
        if(channel.getAdminId().equals(request.getAdminId())){
            if (existsByName(request.getName())) {
                throw new CustomException(ErrorCode.CHANNEL_NAME_ALREADY_EXISTS);
            }

            channel.setChannelName(request.getName());
            channelRepository.update(channel);
        } else {
            throw new CustomException(ErrorCode.NOT_CHANNEL_ADMIN);
        }
    }

    @Override
    public void delete(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHANNEL_NOT_FOUND));

        readStatusRepository.deleteByChannelId(channelId);
        List<UUID> binaryContentIds = messageRepository.deleteByChannelId(channelId); // 채널 메시지 삭제 및 연관 파일 UUID 저장
        binaryContentRepository.deleteByIds(binaryContentIds); // 채널 메시지 연관 파일들 삭제
        channelRepository.deleteById(channelId); // 채널 삭제
    }

    @Override
    public void deleteChannelMember(UUID channelId, UUID requesterId, UUID targetId) {
        userRepository.findById(targetId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHANNEL_NOT_FOUND));

        //삭제 요청 유저와 삭제될 유저가 동일하지 않으면
        if(!requesterId.equals(targetId)){
            //삭제 요청 유저가 관리자가 아니라면 예외 발생
            if(!requesterId.equals(channel.getAdminId())) {
                throw new CustomException(ErrorCode.NOT_CHANNEL_ADMIN);
            // 삭제될 유저가 채널에 속해있지 않으면 예외 발생
            } else if(!channel.getMemberIds().contains(targetId)) {
                throw new CustomException(ErrorCode.NOT_CHANNEL_MEMBER);
            }
        // 삭제될 유저가 관리자인 경우 예외 발생
        } else if(channel.getAdminId().equals(targetId)) {
            throw new CustomException(ErrorCode.CANNOT_LEAVE_AS_CHANNEL_ADMIN);
        }

        // 특정 유저가 나가도 메시지는 채널에 유지
        readStatusRepository.deleteByChannelMember(channelId, targetId);
        channelRepository.deleteMember(channel, targetId);
    }

    @Override
    public boolean isChannelUnavailableForUser(UUID userId, UUID channelId) {
        boolean channelExists = channelRepository.findById(channelId).isPresent();
        boolean userJoined = channelRepository.findAllJoinedByUserId(userId).stream()
                .anyMatch(id -> channelId.equals(id));

        // 채널이 존재하거나 유저가 채널에 속해있다면 false를 반환
        return !(channelExists || userJoined);
    }

    private boolean existsByName(String name) {
        return channelRepository.findAll().stream()
                .filter(c -> c.getChannelName() != null)
                .anyMatch(c -> c.getChannelName().equals(name));
    }
}
