package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.dto.DeletedMessageDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.MessageService;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.ValidateService;

import static com.sprint.mission.discodeit.static_.StaticString.*;

public class JCFMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final ValidateService validateService;

    public JCFMessageService(MessageRepository messageRepository, ValidateService validateService) {
        this.messageRepository = messageRepository;
        this.validateService = validateService;
    }

    public void createMessage(MessageDto messageDto){

        if(messageDto == null){
            System.out.println(NULL_INPUT);
            return;
        }
        if(validateService.isValidateMessage(messageDto)){
            System.out.println(MESSAGE_EXIST+messageDto.getContent() );
            return;
        }
        if(!validateService.isValidateUser(messageDto.getSender())){
            System.out.println(USER_NOT_EXIST + messageDto.getSender().getName());
            return;
        }
        messageRepository.saveMessage(messageDto);
        System.out.println(CREATE_MESSAGE+messageDto.getContent());
    }
    public void readMessage(MessageDto messageDto){
        if(messageDto == null){
            System.out.println(NULL_INPUT);
            return;
        }
        if(!validateService.isValidateMessage(messageDto)){
            System.out.println(MESSAGE_NOT_EXIST+messageDto.getContent());
            return;
        }
        MessageDto result =messageRepository.getMessage(messageDto);
        System.out.println(result.toString());
    }
    public void readAllMessage(){

        for(MessageDto messageDto : messageRepository.getAllMessage()){
            System.out.println(messageDto.toString());
        }
    }
    public void deleteMessage(MessageDto messageDto){


        if(messageDto == null){
            System.out.println(NULL_INPUT);
            return;
        }
        if(!validateService.isValidateMessage(messageDto)){
            System.out.println(MESSAGE_NOT_EXIST+messageDto.getContent());
            return;
        }

        messageRepository.deleteMessage(messageDto);

        System.out.println(DELETE_MESSAGE+messageDto.getContent());
    }
    public void updateMessage(MessageDto messageDto, Message.messageElement messageElement, Object updatedContent){

        if(messageDto == null || updatedContent == null || messageElement == null){
            System.out.println(NULL_INPUT);
            return;
        }
        if(!validateService.isValidateMessage(messageDto)){
            System.out.println(MESSAGE_NOT_EXIST+messageDto.getContent());
            return;
        }
        if(!validateService.isValidateUser(messageDto.getSender())){
            System.out.println(USER_NOT_EXIST + messageDto.getSender().getName());
        }
        messageRepository.updateMessage(messageDto,messageElement,updatedContent);
        System.out.println("Updated field: "+messageElement.name()+"Updated Content: "+updatedContent);
    }
    public void readUpdatedMessage(){
        if(messageRepository.getUpdatedMessage().length == 0){
            System.out.println("NO_UPDATED_MESSAGE");
            return;
        }
        for(MessageDto messageDto : messageRepository.getUpdatedMessage()){
            readMessage(messageDto);
            System.out.println(messageDto.getContent()+" Updated Time: "+messageDto.getUpdatedAt());
        }

    }
    public void readDeletedMessage(){
        if(messageRepository.getDeletedMessage().length == 0){
            System.out.println("NO_DELETED_MESSAGE");
            return;
        }
        System.out.println("===Deleted Message=== ");
        for(DeletedMessageDto deletedMessageDto : messageRepository.getDeletedMessage()){
            ;
            System.out.println(deletedMessageDto.getContent()+" Deleted Time: "+deletedMessageDto.getDeletedTime());
        }
        System.out.println("================");
    }
}
