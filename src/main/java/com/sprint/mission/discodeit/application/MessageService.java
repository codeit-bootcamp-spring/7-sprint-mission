package com.sprint.mission.discodeit.application;

import com.sprint.mission.discodeit.application.dto.request.MessageForm;
import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.Message;
import com.sprint.mission.discodeit.domain.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final ChannelRepository channelRepository;
    private final FileManager fileManager;

    public void sendMessage(MessageForm form) throws IOException {
        Message message;
        if(form.image().isEmpty()){
            message = new Message(form.userId(), form.content(), form.channelId(), null);
        } else {
            BinaryContent content = fileManager.saveMessageFile(form.userId(), form.image());
            message = new Message(form.userId(), form.content(), form.channelId(), content);
        }
        Channel channel = ChannelFindHelper.findById(channelRepository, form.channelId());
        channel.sendMessage(message);
        channelRepository.save(channel);
    }
}
