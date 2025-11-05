package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.dto.Dto_ReadStatus;
import com.sprint.mission.discodeit.entity.dto.Dto_ReadStatusUpdate;
import com.sprint.mission.discodeit.entity.dto.Res_ReadStatus;
import com.sprint.mission.discodeit.service.basic.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequiredArgsConstructor
@RequestMapping("/readStatus")
public class ReadStatusController {
    private final ReadStatusService readStatusService;
//    메시지 수신 정보 관리
//    [ ] 특정 채널의 메시지 수신 정보를 생성할 수 있다.
//    [ ] 특정 채널의 메시지 수신 정보를 수정할 수 있다.
//    [ ] 특정 사용자의 메시지 수신 정보를 조회할 수 있다.
    @RequestMapping(value = "/create", method = POST)
    public Res_ReadStatus create(@RequestBody Dto_ReadStatus dtoReadStatus) {
        return readStatusService.create(dtoReadStatus);
    }
    
    @RequestMapping(value = "/update", method = POST)
    public void update(@RequestBody Dto_ReadStatusUpdate requestDto) {
        readStatusService.update(requestDto);
    }

    @RequestMapping(value = "/findAllByUserId/{id}", method = POST)
    public List<Res_ReadStatus> findAllByUserId(@PathVariable("id") UUID userID) {
        return readStatusService.findAllByUserId(userID);
    }
    
//    @RequestMapping(value = "/delete/{id}", method = DELETE)
//    public void delete(@PathVariable("id") UUID statusID) {}
//
//    @RequestMapping(value = "/find/{id}", method = POST)
//    public Res_ReadStatus find(@PathVariable("id") UUID statusID) {}
}
