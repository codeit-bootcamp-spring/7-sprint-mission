package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.dto.DeletedMessageDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.entity.Message;

import static com.sprint.mission.discodeit.static_.StaticString.*;
import static com.sprint.mission.discodeit.static_.StaticString.CREATE_MESSAGE;
import static com.sprint.mission.discodeit.static_.StaticString.DELETE_MESSAGE;
import static com.sprint.mission.discodeit.static_.StaticString.MESSAGE_NOT_EXIST;
import static com.sprint.mission.discodeit.static_.StaticString.NULL_INPUT;
import static com.sprint.mission.discodeit.static_.StaticString.USER_NOT_EXIST;

public interface MessageService {

    public void createMessage(MessageDto messageDto);
    public void readMessage(MessageDto messageDto);
    public void readAllMessage();
    public void deleteMessage(MessageDto messageDto);
    public void updateMessage(MessageDto messageDto, Message.messageElement messageElement, Object updatedContent);
    public void readUpdatedMessage();
    public void readDeletedMessage();

}
