package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.Docs.MessageControllerDocs;
import com.sprint.mission.discodeit.dto.Binarycontent.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.request.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.request.UpdateMessageRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController implements MessageControllerDocs {

    private final MessageService messageService;

               //[ ] 메시지를 보낼 수 있다.
               @RequestMapping(
                       method = RequestMethod.POST,
                       consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
               public ResponseEntity<Message> createMessage(
                       @RequestPart("messageCreateRequest") CreateMessageRequest request,
                       @RequestPart(value = "attachments", required = false) List<MultipartFile> profiles
               ) {
                   List<BinaryContentCreateRequest> binary = new ArrayList<>();


                   if (profiles != null && !profiles.isEmpty()) {

                       for (MultipartFile profile : profiles) {
                           try {
                               binary.add(new BinaryContentCreateRequest(
                                       profile.getName(),
                                       profile.getContentType(),
                                       profile.getBytes()
                               ));
                           } catch (IOException e) {
                               throw new RuntimeException("파일 처리 못했어", e);
                           }
                       }
                   }

                   Message messageResponse = messageService.create(request, binary);

                   return ResponseEntity
                           .status(HttpStatus.CREATED)
                           .body(messageResponse);
               }

         @RequestMapping(path = "{messageId}",method = RequestMethod.PATCH)
         public ResponseEntity<Message> updateMessage(@PathVariable  UUID messageId,
                                      @RequestBody UpdateMessageRequest request){
             Message update = messageService.update(messageId, request);

             return ResponseEntity
                     .status(HttpStatus.OK)
                     .body(update);
         }
           // [ ] 메시지를 삭제할 수 있다.

           @RequestMapping(path = "{messageId}",method = RequestMethod.DELETE)
           public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId){

                   messageService.delete(messageId);

               return ResponseEntity
                       .status(HttpStatus.NO_CONTENT)
                       .build();
           }

          // [ ] 특정 채널의 메시지 목록을 조회할 수 있다.

       @RequestMapping(method = RequestMethod.GET)
       public ResponseEntity<List<Message>> findAllByChannelId(@RequestParam("channelId") UUID channelId){

           List<Message> allByChannelId = messageService.findAllByChannelId(channelId);

           return ResponseEntity
                           .status(HttpStatus.OK)
                           .body(allByChannelId);
       }

}
