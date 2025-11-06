package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AdvanceController {

    private final UserService userService;
    private final BinaryContentService binaryContentService;
    @RequestMapping(value = "/user/findAll")
    public ResponseEntity<List<UserDto>> advanceFindAllUser(){
        return ResponseEntity.ok(userService.advanceFindAllUser());
    }

    @RequestMapping(value = "/binaryContent/find",method = RequestMethod.GET)
    public ResponseEntity<BinaryContent> advanceFindBinaryContent(@RequestParam UUID binaryContentId){
        return ResponseEntity.ok(binaryContentService.find(binaryContentId));
    }


}
