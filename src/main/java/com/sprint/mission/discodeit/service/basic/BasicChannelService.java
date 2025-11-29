package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.request.ChannelCreateReq;
import com.sprint.mission.discodeit.dto.channel.request.ChannelCreateSecReq;
import com.sprint.mission.discodeit.dto.channel.request.ChannelUpdateReq;
import com.sprint.mission.discodeit.dto.channel.response.ChannelPublicInfoRes;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.facade.mapper.ChannelMapper;
import com.sprint.mission.discodeit.factory.ChannelFactory;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ChannelMemberRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BasicChannelService implements ChannelService {

  //레포지토리
  private final ChannelRepository channelRepository;
  private final ChannelMapper channelMapper;
  private final ChannelMemberRepository channelMemberRepository;

  // ===== 🏗️ Domain Logic (Facade 용)  =====
  //채널 생성
  @Override
  public Channel create(UUID managerId, ChannelCreateReq req) {
    return channelRepository.save(ChannelFactory.create(managerId, req));
  }

  //채널 생성: 비공개
  @Override
  public Channel create(UUID managerId, ChannelCreateSecReq req) {
    return channelRepository.save(ChannelFactory.create(managerId, req));
  }

  //채널 삭제
  @Override
  public void delete(UUID id) {
    if (!channelRepository.existsById(id)) {
      throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
    }
    channelRepository.deleteById(id);
  }

  //채널 목록 : Public 인 경우 전부, Private 인 경우 자신이 참여한 채널만
  @Override
  public Map<ChannelType, List<Channel>> findAllByUserId(UUID userId) {
    return channelRepository.findAll().stream().filter(channel ->
        channel.getPublicType() == ChannelType.PUBLIC ||
            (channel.getPublicType() == ChannelType.PRIVATE &&
                channelMemberRepository.existsByChannel_IdAndUser_Id(channel.getId(), userId)
            )).collect(Collectors.groupingBy(Channel::getPublicType));
  }

  //채널명으로 찾기
  @Override
  public Channel findByName(String name) {
    Optional<Channel> channel = channelRepository.findByNameContaining(name);
    return channel.orElse(null);
  }

  //채널 id 로 조회
  @Override
  public Channel findById(UUID id) {
    return channelRepository.findById(id).orElseThrow(
        () -> new CustomException(ErrorCode.CHANNEL_NOT_FOUND)
    );
  }

  // ===== 🔧 Controller Direct (단일 도메인 / void) =====
  //채널 수정
  @Override
  @Transactional
  public ChannelPublicInfoRes update(@NonNull UUID id, @NonNull ChannelUpdateReq req) {
    Channel channel = channelRepository.findById(id).orElseThrow(
        () -> new CustomException(ErrorCode.CHANNEL_NOT_FOUND)
    );
    if (channel.getPublicType() == ChannelType.PRIVATE) {
      throw new CustomException(ErrorCode.CHANNEL_PRIVATE_CANNOT_MODIFY);
    }
    channel.update(req.name(), req.description());
    return (ChannelPublicInfoRes) channelMapper.toInfoRes(channel);
  }
}
