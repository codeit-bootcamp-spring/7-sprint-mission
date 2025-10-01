package com.sprint.mission.discodeit.service.jcf;



import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.MemoryMessageRoomRepository;
import com.sprint.mission.discodeit.service.MessageRoomService;


import java.util.*;

public class JCFMessageRoomService implements MessageRoomService {

    private final MemoryMessageRoomRepository messageRoomRepository;

    public JCFMessageRoomService(MemoryMessageRoomRepository messageRoomRepository) {
        this.messageRoomRepository=messageRoomRepository;
    }

    @Override
    public void addMessageRoom(MessageRoom messageRoom) {
        messageRoomRepository.save(messageRoom);
    }

    @Override
    public void removeMessageRoom(MessageRoom messageRoom) {
        messageRoomRepository.remove(messageRoom);
    }

    @Override
    public MessageRoom getMessageRoom(UUID id) {
        return messageRoomRepository.findById(id);
    }

    @Override
    public void updateMessageRoom(UUID messageRoomId, MessageRoomDTO messageRoomDTO) {
        messageRoomRepository.updateMessageRoom(messageRoomId, messageRoomDTO);
    }

    //기능추가

    public MessageRoom findOrMakeDM(User user1, User user2){
        List<MessageRoom> messageRooms = messageRoomRepository.findAll();
        MessageRoom messageRoom = messageRooms.stream()
                .filter(m->m.getMessageRoomType()==MessageRoomType.DM)
                .filter(m -> m.getParticipants().contains(user1))
                .filter(m -> m.getParticipants().contains(user2))
                .findFirst()
                .orElse(null);
        if (messageRoom==null){
            MessageRoom newDm = new MessageRoom();
            newDm.setMessageRoomType(MessageRoomType.DM);
            newDm.getParticipants().add(user1);
            newDm.getParticipants().add(user2);
            addMessageRoom(newDm);
            return newDm;
        }
        return messageRoom;
    }

    public MessageRoom makeServerMessageRoom(Channel channel, String name){
        MessageRoom messageRoom = new MessageRoom();
        messageRoom.setMessageRoomType(MessageRoomType.SERVER_MESSAGE_ROOM);
        messageRoom.setMessageRoomName(name);
        List<ChannelUser> members = channel.getMembers();
        addMessageRoom(messageRoom);

        return messageRoom;

    }


}
