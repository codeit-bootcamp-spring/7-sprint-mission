package com.sprint.mission.discodeit.service.jcf;



import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.MessageRoom;
import com.sprint.mission.discodeit.entity.MessageRoomType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageRoomService;


import java.util.*;

public class JCFMessageRoomService implements MessageRoomService {

    private static final Map<UUID, MessageRoom> messageRooms = new HashMap<>();


    @Override
    public void addMessageRoom(MessageRoom messageRoom) {
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
            messageRooms.put(key, messageRoom);
            String s = (messageRoom.getMessageRoomType() == MessageRoomType.DM) ? "[dm]" : "[server]";
            System.out.println("채팅방 생성 성공"+s);
        }

    }

    @Override
    public void removeMessageRoom(MessageRoom messageRoom) {
        UUID key = messageRoom.getId();
        messageRooms.remove(key);
        System.out.println("채팅방 삭제 성공");

    }

    @Override
    public MessageRoom getMessageRoom(MessageRoom messageRoom) {
        UUID key = messageRoom.getId();
        if (!messageRooms.containsKey(key)){
            System.out.println("존재하지 않는 채팅방입니다");
        }
        MessageRoom findMessageRoom = messageRooms.get(key);
        System.out.println("채팅방 찾기 성공");
        return findMessageRoom;
    }

    @Override
    public void updateMessageRoom(UUID messageRoomId, String newName) {
        MessageRoom messageRoom = messageRooms.get(messageRoomId);
        messageRoom.setMessageRoomName(newName);

    }

    public Map<UUID, MessageRoom> getMessageRooms() {
        return messageRooms;
    }

    //기능추가

    public MessageRoom findOrMakeDM(User user1, User user2){
        MessageRoom messageRoom = messageRooms.values().stream().filter(m -> m.getParticipants().size() == 2)
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
        List<User> members = channel.getMembers();
        List<User> participants = messageRoom.getParticipants();
        members.forEach(u->participants.add(u));
        addMessageRoom(messageRoom);

        return messageRoom;

    }


}
