package com.sprint.mission.discodeit.service.mapper;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.dto.response.MessageDto;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-25T16:24:39+0900",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.16 (Eclipse Adoptium)"
)
@Component
public class MessageMapperImpl implements MessageMapper {

    @Autowired
    private UserMapper userMapper;

    @Override
    public MessageDto toDto(Message message) {
        if ( message == null ) {
            return null;
        }

        MessageDto messageDto = new MessageDto();

        messageDto.setAuthor( userMapper.toDto( message.getUser() ) );
        messageDto.setChannelId( messageChannelId( message ) );
        messageDto.setId( message.getId() );
        messageDto.setCreatedAt( message.getCreatedAt() );
        messageDto.setUpdatedAt( message.getUpdatedAt() );
        messageDto.setContent( message.getContent() );

        return messageDto;
    }

    private UUID messageChannelId(Message message) {
        Channel channel = message.getChannel();
        if ( channel == null ) {
            return null;
        }
        return channel.getId();
    }
}
