package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Entity;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.function.BiConsumer;

public class JCFMessage implements MessageService {
    public final ArrayList<Message> messageDB ;
    final ArrayList<String> deletedMessage = new ArrayList<>();
    private final JCFUser userService;


    public JCFMessage(JCFUser userService) {
        this.messageDB = new ArrayList<>();
        this.userService = userService;
    }

    public JCFMessage(JCFUser userService,ArrayList<Message> messageDB) {
        this.messageDB = messageDB;
        this.userService = userService;
    }

    @Override
    public void createMessage(Message message) {
        if(
                userService.userDB.stream()
                        .noneMatch(x->x==message.getSender())

        ) {
            System.out.println("그런 유저는 존재하지 않습니다. 꿈 깨시죠");
            return;
        }
        messageDB.add(message);
        System.out.printf("Message created: %s\n", message.getSender().getName()+" : " +message.getContent());

    }

    @Override
    public void readMessage(Message message) {
        if(messageDB.stream()
                .noneMatch(m->m.getContent().equals(message.getContent()))){
            System.out.println("Message Not Found");
        }else{
            System.out.println(message.toString());
        }

    }

    public void readMessage(Message ... messages){

        for(Message message : messages){
         if (messageDB.stream()
                    .noneMatch(m->m.getContent().equals(message.getContent()))){
                System.out.println("Message Not Found");
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
        if (messageDB.stream()
                .noneMatch(m -> m.getContent().equals(message.getContent()))) {
            System.out.println("No such message");
            return;
        }
        messageDB.remove(message);
        deletedMessage.add(message.getContent());
        System.out.printf("Message deleted: %s\n", message.getContent());
    }

    @Override
    public <T> void updateMessage(Message message, Message.messageElement messageElement,T updatedContent) {
        BiConsumer<Message, Object> editFunction = messageElement.setter;
        Class<? extends BiConsumer> aClass = editFunction.getClass();
        if(!aClass.isInstance(updatedContent)) {
            System.out.println("잘못된 타입을 입력했습니다");
            return;
        }
        editFunction.accept(message, updatedContent);
        message.updateEntity();
        System.out.printf("Message updated: %s\n", message.getContent());

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
                System.out.println(message.getContent() + " is Updated at: " + " " + message.getUpdatedAt());
            }
        }

    }

    @Override
    public void readDeletedMessage() {
        System.out.println("Deleted Message = " + deletedMessage);

    }
}
