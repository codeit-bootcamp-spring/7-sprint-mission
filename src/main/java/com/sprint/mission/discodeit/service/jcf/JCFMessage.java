package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Entity;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

public class JCFMessage implements MessageService {
    private final ArrayList<Message> messageDB;
    private final Map<UUID,String> deletedMessage = new HashMap<>();
    private final ArrayList<User> userDb;

    private final JCFDb jcfDb;



    public JCFMessage(JCFDb jcfDb) {
        this.jcfDb = jcfDb;
        this.messageDB = jcfDb.getMessageDb();
        this.userDb = jcfDb.getUserDb();
    }

    @Override
    public void createMessage(Message message) {
        if (userDb.stream()
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

//    public void changeToDeletedUser(Message msg){
//        if(jcfDb.getDeletedUserDb().containsKey(msg.getSender().getId()))
//        {
//            msg.setSender(JCFUser.DELETED_USER);
//            return;
//        }
//        System.out.println("아직 유저가 죽지 않앗습니다.");
//    }
}
