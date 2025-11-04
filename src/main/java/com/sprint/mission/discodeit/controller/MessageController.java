package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.message.request.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.request.DeleteMessageRequest;
import com.sprint.mission.discodeit.dto.message.request.UpdateMessageRequest;
import com.sprint.mission.discodeit.dto.message.request.FindAllByChannelIdMessageRequest;
import com.sprint.mission.discodeit.dto.message.response.MessageResponse;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
@AllArgsConstructor
public class MessageController {

    private final MessageService messageService;

               //[ ] 메시지를 보낼 수 있다.
         @PostMapping("/create")
         public MessageResponse createMessage(@RequestBody CreateMessageRequest request){
               return   messageService.create(request);
          }
           // [ ] 메시지를 수정할 수 있다.
         @PostMapping("/update")
         public MessageResponse updateMessage(@RequestBody UpdateMessageRequest request){
                   return messageService.update(request);
           }
           // [ ] 메시지를 삭제할 수 있다.
           @DeleteMapping("/delete")
           public void deleteMessage(@RequestBody DeleteMessageRequest request){
               messageService.delete(request);
           }
          // [ ] 특정 채널의 메시지 목록을 조회할 수 있다.
       @PostMapping("/findallbychannelid")
       public List<MessageResponse> findAllByChannelId(@RequestBody FindAllByChannelIdMessageRequest request){
           return messageService.findAllByChannelId(request);
       }

}
