//package com.sprint.mission.discodeit.controller;
//
//import com.sprint.mission.discodeit.entity.dto.Dto_BinaryContent;
//import com.sprint.mission.discodeit.entity.dto.Dto_Message;
//import com.sprint.mission.discodeit.entity.dto.Dto_MessageUpdate;
//import com.sprint.mission.discodeit.entity.dto.Res_Message;
//import com.sprint.mission.discodeit.service.basic.MessageService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//@RestController
//@RequiredArgsConstructor
//// http://localhost:8080/user-list.html
//public class MessageController  extends BaseController {
//    private final MessageService messageService;
//
////    메시지 관리
////    [ ] 메시지를 보낼 수 있다.
////    [ ] 메시지를 수정할 수 있다.
////    [ ] 메시지를 삭제할 수 있다.
////    [ ] 특정 채널의 메시지 목록을 조회할 수 있다.
//    @RequestMapping(value = "message/create", method = RequestMethod.POST)
//    public Res_Message create(Dto_Message dtoMessage, Optional<List<Dto_BinaryContent>> requestDto) {
//
//    }
//
//    @RequestMapping(value = "message/findallByChannleId", method = RequestMethod.POST)
//    public List<Res_Message> findallByChannleId(UUID channelID) {
//
//    }
//
//    @RequestMapping(value = "message/find", method = RequestMethod.POST)
//    public Res_Message find(UUID messageID) {
//
//    }
//
//    @RequestMapping(value = "message/update", method = RequestMethod.POST)
//    public Res_Message updateMessage(Dto_MessageUpdate requestDto) {
//
//    }
//
//    @RequestMapping(value = "message/delete", method = RequestMethod.POST)
//    public void deleteMessage(UUID messageID) {
//
//    }
//}
