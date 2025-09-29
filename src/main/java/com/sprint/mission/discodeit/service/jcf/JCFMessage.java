package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Entity;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

public class JCFMessage implements MessageService {
    public final ArrayList<Message> messageDB;
    final Map<UUID,String> deletedMessage = new HashMap<>();
    private final JCFUser userService;


    public JCFMessage(JCFUser userService) {
        this.messageDB = new ArrayList<>();
        this.userService = userService;
    }

    public JCFMessage(JCFUser userService, ArrayList<Message> messageDB) {
        this.messageDB = messageDB;
        this.userService = userService;
    }

    @Override
    public void createMessage(Message message) {
        if (
                userService.userDB.stream()
                        .noneMatch(x -> x.getId() == message.getSender().getId())

        ) {
            System.out.println("유저는 존재하지 않습니다. : " + message.getSender().getName() );
            return;
        }
        messageDB.add(message);
        System.out.printf("메세지가 생성되었습니다 : %s\n", message.getSender().getName() + " : " + message.getContent());

    }

    @Override
    public void readMessage(Message message) {
        if(deletedMessage.containsKey(message.getId())){
            System.out.println(message.getContent() +" 삭제된 메시지입니다.");
            return;
        }
        if (messageDB.stream()
                .noneMatch(m -> m.getId() == message.getId())) {
            System.out.println("메세지는 존재하지 않습니다 : "+ message.getContent());
            return;
        }
        changeToDeletedUser(message);

            System.out.println(message.toString());


    }

    public void readMessage(Message... messages) {

        for (Message message : messages) {
            if(deletedMessage.containsKey(message.getId())){
                System.out.println(message.getContent() +" 삭제된 메시지입니다.");
                continue;
            }
            if (messageDB.stream()
                    .noneMatch(m -> m.getId() == message.getId())) {
                System.out.println("메세지는 존재하지 않습니다: " + message.getContent( ));
                continue;
            }

            changeToDeletedUser(message);
            System.out.println(message.toString());
        }
    }

    @Override
    public void readAllMessage() {
        for (Message message : messageDB) {
            readMessage(message);
        }

    }

    @Override
    public void deleteMessage(Message message) {
        Message finalMessage = message;
        if (messageDB.stream()
                .noneMatch(m->m.getId()== finalMessage.getId())) {
            System.out.println(" 메세지는 존재하지 않습니다. : "+ message.getContent());
            return;
        }
        changeToDeletedUser(message);
        messageDB.remove(message);
        deletedMessage.put(message.getId(),message.getContent());
        System.out.printf("메세지를 삭제합니다: %s\n", message.getContent());
    }

    @Override
    public <T> void updateMessage(Message message, Message.messageElement messageElement, T updatedContent) {

        if (messageDB.stream()
                .noneMatch(m->m.getId()==message.getId())) {
            System.out.println(" 메세지는 존재하지 않습니다. : "+ message.getContent());
            return;
        }
        changeToDeletedUser(message);
        BiConsumer<Message, Object> editFunction = messageElement.setter;
      try{
          editFunction.accept(message, updatedContent);
          message.updateEntity();
          System.out.printf("메세지를 변경했습니다.: %s\n", message.getContent());
      }
      catch (ClassCastException e){
          System.out.println("잘못된 타입을 입력했습니다. 올바른 입력값을 넣어주세요");
      }


    }

    @Override
    public void readUpdatedMessage() {
        if (messageDB.stream().noneMatch(m -> m.getUpdatedAt() != Entity.DEFAULT_UPDATED_AT)) {
            System.out.println("업데이트 된 메시지가 없습니다.");
            return;
        }
        for (Message message : messageDB) {
            changeToDeletedUser(message);
            if (message.getUpdatedAt() != Entity.DEFAULT_UPDATED_AT) {
                readMessage(message);
                System.out.println(message.getContent() + " 변경 시간: " + " " + message.getUpdatedAt());
            }
        }

    }

    @Override
    public void readDeletedMessage() {
        if (deletedMessage.isEmpty()) {
            System.out.println( "삭제된 메세지가 없습니다.");
            return;
        }
        System.out.println( "===삭제된 메세지=== ");
        for( UUID tmp :deletedMessage.keySet()){
            String value = deletedMessage.get(tmp);
            System.out.println(value);
        }
        System.out.println("==========");
//        System.out.println("삭제된 메세지 : " + deletedMessage);

    }

    public void changeToDeletedUser(Message msg){
        if(userService.deletedUser.containsKey(msg.getSender().getId()))
        {
            msg.setSender(JCFUser.DELETED_USER);
        }
    }
}
