package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.Binarycontent.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.request.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.request.DeleteMessageRequest;
import com.sprint.mission.discodeit.dto.message.request.UpdateMessageRequest;
import com.sprint.mission.discodeit.dto.message.request.FindAllByChannelIdMessageRequest;
import com.sprint.mission.discodeit.dto.message.response.MessageResponse;
import com.sprint.mission.discodeit.entity.content.ContentsType;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

               //[ ] 메시지를 보낼 수 있다.
               @RequestMapping(value = "/create", method = RequestMethod.POST)
               public MessageResponse createMessage(
                       @RequestParam("message") CreateMessageRequest request,
                       @RequestParam(value = "profiles", required = false) List<MultipartFile> profiles
               ) {
                   List<BinaryContentCreateRequest> binarys = new ArrayList<>();

                   // 여러 개의 파일이 존재한다면 각각 변환
                   if (profiles != null && !profiles.isEmpty()) {
                       for (MultipartFile profile : profiles) {
                           try {
                               binarys.add(new BinaryContentCreateRequest(
                                       ContentsType.PROFILE_IMAGE,
                                       profile.getBytes()
                               ));
                           } catch (IOException e) {
                               throw new RuntimeException("파일 처리 못했어", e);
                           }
                       }
                   }

                   // 여러 개면 optional이 아니라 List로 넘기는 게 자연스럽겠죠
                   return messageService.create(request, binarys);
               }

         @RequestMapping(value = "/update", method = RequestMethod.PATCH)
         public MessageResponse updateMessage(@RequestBody UpdateMessageRequest request){
                   return messageService.update(request);
           }
           // [ ] 메시지를 삭제할 수 있다.

           @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
           public void deleteMessage(@RequestBody DeleteMessageRequest request){
               messageService.delete(request);
           }

          // [ ] 특정 채널의 메시지 목록을 조회할 수 있다.

       @RequestMapping(value = "/findallbychannelid", method = RequestMethod.POST)
       public List<MessageResponse> findAllByChannelId(@RequestBody FindAllByChannelIdMessageRequest request){
           return messageService.findAllByChannelId(request);
       }

}
