package com.sprint.mission.discodeit.service.mapper;

import com.sprint.mission.discodeit.domain.Message;
import com.sprint.mission.discodeit.entity.MessageEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    Message toMessage(MessageEntity messageEntity);

    MessageEntity toMessageEntity(Message message);
}
