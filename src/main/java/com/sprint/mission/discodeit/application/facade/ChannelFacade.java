package com.sprint.mission.discodeit.application.facade;

import com.sprint.mission.discodeit.application.BasicChannelService;
import com.sprint.mission.discodeit.application.ChannelMemberService;
import com.sprint.mission.discodeit.application.MessageService;
import com.sprint.mission.discodeit.application.dto.request.ChannelRequestDto;
import com.sprint.mission.discodeit.application.dto.request.MessageForm;
import com.sprint.mission.discodeit.application.dto.response.ChannelResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChannelFacade {

    private final BasicChannelService basicChannelService;
    private final MessageService messageService;
    private final ChannelMemberService channelMemberService;

    //BasicChannelService
    public List<ChannelResponseDto> findAllByServer(UUID serverId) {return basicChannelService.findAllByServer(serverId);}
    public ChannelResponseDto updateChannel(ChannelRequestDto requestDto) {return basicChannelService.updateChannel(requestDto);}
    public void deleteAllByServer(UUID serverId) {basicChannelService.deleteAllByServer(serverId);}
    public void deleteChannel(UUID channelId) {basicChannelService.deleteChannel(channelId);}

    //MessageService
    public void sendMessage(MessageForm form) throws IOException {messageService.sendMessage(form);}

    //ChannelMemberService
    public void addChannelMember(UUID channelId,UUID userId){channelMemberService.addChannelMember(channelId,userId);}
}
