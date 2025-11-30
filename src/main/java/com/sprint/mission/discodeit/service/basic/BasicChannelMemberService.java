package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channelmember.response.ChannelMemberInfoRes;
import com.sprint.mission.discodeit.entity.ChannelMember;
import com.sprint.mission.discodeit.entity.ChannelMemberRole;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.ChannelMemberRepository;
import com.sprint.mission.discodeit.service.ChannelMemberService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicChannelMemberService implements ChannelMemberService {

  private final ChannelMemberRepository channelMemberRepository;

  // ===== 🏗️ Domain Logic (Facade 용)  =====
  @Override
  public ChannelMember create(ChannelMember channelMember) {
    //유저id 랑 channel id 가 이미 있는 readStatus 가 있으면 에러
    if (channelMemberRepository.existsByChannel_IdAndUser_Id(
        channelMember.getChannel().getId(), channelMember.getUser().getId()
    )) {
      throw new CustomException(ErrorCode.READSTATUS_ALREADY_EXISTS);
    }

    return channelMemberRepository.save(channelMember);
  }

  @Override
  @Transactional
  public ChannelMember update(UUID id) {
    ChannelMember channelMember = channelMemberRepository.findById(id).orElseThrow(
        () -> new CustomException(ErrorCode.READSTATUS_NOT_FOUND)
    );
    channelMember.updateReadAt();
    return channelMember;
  }

  @Override
  public void delete(UUID id) {
    if (!channelMemberRepository.existsById(id)) {
      throw new CustomException(ErrorCode.READSTATUS_NOT_FOUND);
    }
    channelMemberRepository.deleteById(id);
  }

  @Override
  public ChannelMember findManagerByChannelId(UUID channelId) {
    return channelMemberRepository.findByChannel_IdAndRole(channelId, ChannelMemberRole.MANAGER)
        .stream().findFirst().orElseThrow(
            () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
  }

  @Override
  public List<ChannelMember> findMembersByChannelId(UUID channelId) {
    return channelMemberRepository.findByChannel_IdAndRole(channelId, ChannelMemberRole.MEMBER)
        .stream().toList();
  }

  @Override
  public List<ChannelMember> findAllByChannelId(UUID channelId) {
    return channelMemberRepository.findAllByChannel_id(channelId);
  }

  // ===== 🎯 Controller Direct (DTO 반환) =====
  @Override
  @Transactional(readOnly = true)
  public ChannelMemberInfoRes findById(UUID id) {
    ChannelMember channelMember = channelMemberRepository.findById(id).orElseThrow(() ->
        new CustomException(ErrorCode.READSTATUS_NOT_FOUND));
    return ChannelMemberInfoRes.from(channelMember);
  }
}
