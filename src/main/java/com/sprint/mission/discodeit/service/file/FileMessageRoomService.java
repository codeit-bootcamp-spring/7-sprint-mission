package com.sprint.mission.discodeit.service.file;


import com.sprint.mission.discodeit.DTO.MessageRoomDTO;
import com.sprint.mission.discodeit.entity.MessageRoom;
import com.sprint.mission.discodeit.service.MessageRoomService;
import com.sprint.mission.discodeit.service.repository.MessageRoomRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class FileMessageRoomService implements MessageRoomService {

    private final MessageRoomRepository messageRoomRepository;

    public FileMessageRoomService(MessageRoomRepository messageRoomRepository) {
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
    public void update(UUID id, MessageRoomDTO messageRoomDTO) {
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
