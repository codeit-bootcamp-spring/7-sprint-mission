package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ApiResponseDto;
import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
//    @RequestMapping(value = "/user/findAll")
//    public ResponseEntity<List<ApiResponseDto<UserDto>>> advanceFindAllUser(){
//        List<ApiResponseDto<UserDto>> apiResponseDtoList = userService.advanceFindAllUser().stream().map(ApiResponseDto::success).toList();
//        return new ResponseEntity<List<ApiResponseDto<UserDto>>>(apiResponseDtoList, HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/binaryContent/find",method = RequestMethod.GET)
//    public ResponseEntity<ApiResponseDto<BinaryContent>> advanceFindBinaryContent(@RequestParam UUID binaryContentId){
//        ApiResponseDto<BinaryContent> apiResponseDto = ApiResponseDto.success(binaryContentService.find(binaryContentId));
//        return new ResponseEntity<ApiResponseDto<BinaryContent>>(apiResponseDto,HttpStatus.OK );
//    }
//

}
