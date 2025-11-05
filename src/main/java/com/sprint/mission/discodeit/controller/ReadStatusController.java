//package com.sprint.mission.discodeit.controller;
//
//import com.sprint.mission.discodeit.entity.dto.Dto_ReadStatus;
//import com.sprint.mission.discodeit.entity.dto.Dto_ReadStatusUpdate;
//import com.sprint.mission.discodeit.entity.dto.Res_ReadStatus;
//import com.sprint.mission.discodeit.service.basic.ReadStatusService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//import java.util.UUID;
//
//@RestController
//@RequiredArgsConstructor
//// http://localhost:8080/user-list.html
//public class ReadStatusController  extends BaseController {
//    private final ReadStatusService readStatusService;
////    메시지 수신 정보 관리
////    [ ] 특정 채널의 메시지 수신 정보를 생성할 수 있다.
////    [ ] 특정 채널의 메시지 수신 정보를 수정할 수 있다.
////    [ ] 특정 사용자의 메시지 수신 정보를 조회할 수 있다.
//
//    @RequestMapping(value = "readStatus/delete", method = RequestMethod.POST)
//    public void delete(UUID statusID) {}
//
//    @RequestMapping(value = "readStatus/update", method = RequestMethod.POST)
//    public void update(Dto_ReadStatusUpdate requestDto) {}
//
//    @RequestMapping(value = "readStatus/findAllByUserId", method = RequestMethod.POST)
//    public List<Res_ReadStatus> findAllByUserId(UUID userID) {}
//
//    @RequestMapping(value = "readStatus/find", method = RequestMethod.POST)
//    public Res_ReadStatus find(UUID statusID) {}
//
//    @RequestMapping(value = "readStatus/create", method = RequestMethod.POST)
//    public Res_ReadStatus create(Dto_ReadStatus dtoReadStatus) {}
//}
