package com.sprint.mission.discodeit.messageroom.application;



import com.sprint.mission.discodeit.messageroom.domain.MessageRoom;
import com.sprint.mission.discodeit.messageroom.presentation.MessageRoomService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BasicMessageRoomService implements MessageRoomService {

    private final MessageRoomRepository messageRoomRepository;

    public BasicMessageRoomService(MessageRoomRepository messageRoomRepository) {
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

}
