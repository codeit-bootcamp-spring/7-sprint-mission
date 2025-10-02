package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Entity;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ValidateService;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static com.sprint.mission.discodeit.static_.StaticString.*;

public class JCFMessage implements MessageService {
    private final ArrayList<Message> messageDb;
    private final Map<UUID,String> deletedMessageDb ;
    private final ArrayList<User> userDb;
    private final ValidateService validateService;

    private final JCFDb jcfDb;



    public JCFMessage(JCFDb jcfDb) {
        this.jcfDb = jcfDb;
        this.messageDb = jcfDb.getMessageDb();
        this.userDb = jcfDb.getUserDb();
        this.validateService = new JCFValidateOperator(jcfDb);
        this.deletedMessageDb = jcfDb.getDeletedMessageDb();
    }

    @Override
    public void createMessage(Message message) {
        if (message == null) {
            System.out.println(NULL_INPUT);
            return;
        }
        if(validateService.isValidateMessage(message)){
            System.out.println(MESSAGE_EXIST + message.getContent());
            return;
        }
        if(!validateService.isValidateUser(message.getSender())){
            System.out.println(USER_NOT_EXIST + message.getSender().getName());
            return;
        }
        messageDb.add(message);
        System.out.println(CREATE_MESSAGE + message.getContent());
        return;
    }

    @Override
    public void readMessage(Message message) {
        if (message == null) {
            System.out.println(NULL_INPUT);
            return;
        }
        if(deletedMessageDb.containsKey(message.getId())){
            System.out.println(message.getContent() +MESSAGE_ALREADY_DELETED);
            return;
        }
        if (messageDb.stream()
                .noneMatch(m -> m.getId() == message.getId())) {
            System.out.println(MESSAGE_NOT_EXIST+ message.getContent());
            return;
        }


            System.out.println(message.toString());


    }

    public void readMessage(Message... messages) {

        for (Message message : messages) {

            if(deletedMessageDb.containsKey(message.getId())){
                System.out.println(message.getContent() +MESSAGE_ALREADY_DELETED);
                continue;
            }
            if (messageDb.stream()
                    .noneMatch(m -> m.getId() == message.getId())) {
                System.out.println(MESSAGE_NOT_EXIST + message.getContent( ));
                continue;
            }


            System.out.println(message.toString());
        }
    }

    @Override
    public void readAllMessage() {
        if(messageDb.isEmpty()){
            System.out.println(MESSAGE_EMPTY);
            return;
        }
        for (Message message : messageDb) {
            readMessage(message);
        }

    }

    @Override
    public void deleteMessage(Message message) {
        if (message == null) {
            System.out.println(NULL_INPUT);
            return;
        }
        if(!validateService.isValidateMessage(message)){
            System.out.println(MESSAGE_NOT_EXIST + message.getContent());
            return;
        }
        messageDb.remove(message);
        deletedMessageDb.put(message.getId(),message.getContent());
        System.out.println(DELETE_MESSAGE + message.getContent());
        return;
    }

    @Override
    public <T> void updateMessage(Message message, Message.messageElement messageElement, T updatedContent) {
        if (message == null || updatedContent == null || messageElement == null) {
            System.out.println(NULL_INPUT);
            return;
        }

        if (messageDb.stream()
                .noneMatch(m->m.getId()==message.getId())) {
            System.out.println(MESSAGE_NOT_EXIST+ message.getContent());
            return;
        }


      try{
          BiConsumer<Message, Object> editFunction = messageElement.setter;
          Object oldContent = messageElement.getter.apply(message);
          editFunction.accept(message, updatedContent);
          message.updateEntity();
          System.out.println(DELETE_MESSAGE+ message.getContent());
          System.out.println("변경한 필드: "+ messageElement.name()+ " 변경전: "+oldContent +" ==> 변경 후: "+updatedContent);

      }
      catch (ClassCastException e){
          System.out.println(WRONG_TYPE);
      }


    }

    @Override
    public void readUpdatedMessage() {
        if (messageDb.stream().noneMatch(m -> m.getUpdatedAt() != Entity.DEFAULT_UPDATED_AT)) {
            System.out.println("업데이트 된 메시지가 없습니다.");
            return;
        }
        for (Message message : messageDb) {

            if (message.getUpdatedAt() != Entity.DEFAULT_UPDATED_AT) {
                readMessage(message);
                System.out.println(message.getContent() + " 변경 시간: " + " " + message.getUpdatedAt());
            }
        }

    }

    @Override
    public void readDeletedMessage() {
        if (deletedMessageDb.isEmpty()) {
            System.out.println( "삭제된 메세지가 없습니다.");
            return;
        }
        System.out.println( "===삭제된 메세지=== ");
        for( UUID tmp :deletedMessageDb.keySet()){
            String value = deletedMessageDb.get(tmp);
            System.out.println(value);
        }
        System.out.println("==========");
//        System.out.println("삭제된 메세지 : " + deletedMessage);

    }

//    public <T>Object getMessageElement(Message message, Message.messageElement messageElement){
//        switch (messageElement){
//            case CONTENT:
//                return message.getContent();
//            case IS_MARKDOWN:
//                return message.isMarkDown();
//            default:
//                throw new IllegalArgumentException(WRONG_TYPE);
//        }
//    }

//    public void changeToDeletedUser(Message msg){
//        if(jcfDb.getDeletedUserDb().containsKey(msg.getSender().getId()))
//        {
//            msg.setSender(JCFUser.DELETED_USER);
//            return;
//        }
//        System.out.println("아직 유저가 죽지 않앗습니다.");
//    }
}
