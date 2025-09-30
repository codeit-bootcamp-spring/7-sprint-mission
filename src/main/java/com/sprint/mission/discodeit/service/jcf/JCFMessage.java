package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Entity;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.etc.StaticString;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

import static com.sprint.mission.discodeit.etc.StaticString.*;

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
            System.out.println(USER_NOT_EXIST + message.getSender().getName() );
            return;
        }
        messageDB.add(message);
        System.out.printf(CREATE_MESSAGE+ message.getSender().getName() + " : " + message.getContent());

    }

    @Override
    public void readMessage(Message message) {
        if(deletedMessage.containsKey(message.getId())){
            System.out.println(message.getContent() +MESSAGE_ALREADY_DELETED);
            return;
        }
        if (messageDB.stream()
                .noneMatch(m -> m.getId() == message.getId())) {
            System.out.println(MESSAGE_NOT_EXIST+ message.getContent());
            return;
        }


            System.out.println(message.toString());


    }

    public void readMessage(Message... messages) {

        for (Message message : messages) {
            if(deletedMessage.containsKey(message.getId())){
                System.out.println(message.getContent() +MESSAGE_ALREADY_DELETED);
                continue;
            }
            if (messageDB.stream()
                    .noneMatch(m -> m.getId() == message.getId())) {
                System.out.println(MESSAGE_NOT_EXIST + message.getContent( ));
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
            System.out.println(MESSAGE_NOT_EXIST+ message.getContent());
            return;
        }

        messageDB.remove(message);
        deletedMessage.put(message.getId(),message.getContent());
        System.out.printf(DELETE_MESSAGE+ message.getContent());
    }

    @Override
    public <T> void updateMessage(Message message, Message.messageElement messageElement, T updatedContent) {

        if (messageDB.stream()
                .noneMatch(m->m.getId()==message.getId())) {
            System.out.println(MESSAGE_NOT_EXIST+ message.getContent());
            return;
        }

        BiConsumer<Message, Object> editFunction = messageElement.setter;
        Object oldContent = getMessageElement(message,messageElement);
      try{
          System.out.printf(DELETE_MESSAGE+ message.getContent());
          System.out.println("변경한 필드: "+ messageElement.name()+ " 변경전: "+oldContent +" ==> 변경 후: "+updatedContent);
          editFunction.accept(message, updatedContent);
          message.updateEntity();

      }
      catch (ClassCastException e){
          System.out.println(WRONG_TYPE);
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

    public <T>Object getMessageElement(Message message, Message.messageElement messageElement){
        switch (messageElement){
            case CONTENT:
                return message.getContent();
            case IS_MARKDOWN:
                return message.isMarkDown();
            default:
                throw new IllegalArgumentException(WRONG_TYPE);
        }
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
