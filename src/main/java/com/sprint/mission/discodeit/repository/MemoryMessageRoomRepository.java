package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.MessageRoom;
import com.sprint.mission.discodeit.entity.MessageRoomDTO;
import com.sprint.mission.discodeit.entity.MessageRoomType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MemoryMessageRoomRepository {

    private final Map<UUID, MessageRoom> store= new HashMap<>();

    public void save(MessageRoom messageRoom){
        boolean flag = true;
        if (messageRoom.getMessageRoomType()==null){
            flag=false;
            System.out.println("채팅방 타입을 설정하십시오");
        }
        if (messageRoom.getParticipants().size()<1){
            flag=false;
            System.out.println("채팅방 인원이 아무도 없습니다");
        }
        if (flag==true){
            UUID key = messageRoom.getId();
            store.put(key, messageRoom);
            String s = (messageRoom.getMessageRoomType() == MessageRoomType.DM) ? "[dm]" : "[server]";
            System.out.println("채팅방 생성 성공"+s);
        }
    }

    public void remove(MessageRoom messageRoom){
        UUID key = messageRoom.getId();
        store.remove(key);
        System.out.println("채팅방 삭제 성공");
    }

    public MessageRoom findById(UUID id){
        MessageRoom findMessageRoom = store.get(id);
        System.out.println("채팅방 찾기 성공");
        return findMessageRoom;
    }

    public List<MessageRoom> findAll(){
        return store.values().stream().toList();
    }

    public void updateMessageRoom(UUID id, MessageRoomDTO messageRoomDTO){
        MessageRoom messageRoom = store.get(id);
        if(messageRoomDTO.getMessageRoomName()!=null){
            messageRoom.setMessageRoomName(messageRoomDTO.getMessageRoomName());
        }
    }
}
