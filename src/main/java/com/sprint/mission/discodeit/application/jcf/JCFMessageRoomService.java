package com.sprint.mission.discodeit.application.jcf;



import com.sprint.mission.discodeit.dto.MessageRoomDto;
import com.sprint.mission.discodeit.domain.*;
import com.sprint.mission.discodeit.application.repository.MessageRoomRepository;
import com.sprint.mission.discodeit.application.MessageRoomService;


import java.util.*;

public class JCFMessageRoomService implements MessageRoomService {

    private final MessageRoomRepository messageRoomRepository;

    public JCFMessageRoomService(MessageRoomRepository messageRoomRepository) {
        this.messageRoomRepository=messageRoomRepository;
    }

    @Override
    public void save(MessageRoom messageRoom) {
        messageRoomRepository.save(messageRoom);
        System.out.println("채팅방 등록 성공");
    }

    @Override
    public void remove(MessageRoom messageRoom) {
        messageRoomRepository.remove(messageRoom);
        System.out.println("채팅방 삭제 성공");
    }

    @Override
    public MessageRoom findById(UUID uuid) {

        return messageRoomRepository.findById(uuid).orElseThrow(()->new NoSuchElementException("채팅방을 찾을 수 없습니다."));
    }

    @Override
    public List<MessageRoom> findAll() {
        return messageRoomRepository.findAll();
    }

    @Override
    public void update(UUID id, MessageRoomDto messageRoomDTO) {
        MessageRoom messageRoom = findById(id);
        if(messageRoomDTO.getMessageRoomName()!=null){
            messageRoom.setMessageRoomName(messageRoomDTO.getMessageRoomName());
        }
        save(messageRoom);
    }
//    //서버 채팅방 만들기
//    public MessageRoom makeChannelMessageRoom(Channel channel){
//        MessageRoom messageRoom = new MessageRoom();
//        messageRoom.setMessageRoomType(MessageRoomType.SERVER_MESSAGE_ROOM);
//        List<UUID> members = channel.getMembers();
//        for (UUID member : members) {
//            messageRoom.addParticipants(member);
//        }
//        return messageRoom;
//    }

}
