package com.sprint.mission.discodeit.facade.channelmember;

import com.sprint.mission.discodeit.dto.channelmember.request.ChannelMemberCreateReq;
import com.sprint.mission.discodeit.entity.ChannelMember;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.factory.ChannelMemberFactory;
import com.sprint.mission.discodeit.service.ChannelMemberService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChannerMemberCreateFacade {

  private final ChannelMemberService channelMemberService;
  private final ChannelService channelService;
  private final UserService userService;
  private final ChannelMemberFactory channelMemberFactory;

  public ChannelMember create(@NonNull ChannelMemberCreateReq req) {
    if (channelService.findById(req.channelId()) == null) {
      throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
    }
    if (userService.findById(req.userId()) == null) {
      throw new CustomException(ErrorCode.USER_NOT_FOUND);
    }
    return channelMemberService.create(channelMemberFactory.create(req));
  }
}
