package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ApiResponseDto;
import com.sprint.mission.discodeit.dto.request.readStatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.readStatus.ReadStatusPatchRequestDto;
import com.sprint.mission.discodeit.dto.request.readStatus.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.ReadStatusResponseDto;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/readStatuses")
@RequiredArgsConstructor
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<ReadStatusResponseDto> createReadStatus(@RequestBody ReadStatusCreateRequestDto dto){

        return new ResponseEntity<ReadStatusResponseDto>(readStatusService.createReadStatus(
                dto
        ), HttpStatus.CREATED);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<List<ReadStatusResponseDto>> readReadStatus(@RequestParam UUID userId){
        return new ResponseEntity<List<ReadStatusResponseDto>>(readStatusService.findAllyByUserId(userId), HttpStatus.OK);
    }

    @RequestMapping(value = "/{readStatusId}",method = RequestMethod.PATCH)
    public ResponseEntity<ReadStatusResponseDto> patchReadStatus(@PathVariable UUID readStatusId, @RequestBody ReadStatusPatchRequestDto dto)
    {
     return new ResponseEntity<>(readStatusService.patchReadStatus(readStatusId, dto), HttpStatus.OK);
    }
    /// ////////////////////////////////////////////////////////////////////////////////////////////////
//
//    @RequestMapping(value = "/update", method = RequestMethod.POST)
//    public <T>void updateReadStatus(@RequestBody ReadStatusUpdateRequestDto<T> dto){
//        readStatusService.updateReadStatus(dto);
//    }
//    @RequestMapping("/readAll")
//    public ResponseEntity<List<ApiResponseDto<ReadStatusResponseDto>>> readAll(){
//        List<ApiResponseDto<ReadStatusResponseDto>> apiResponseDtoList = readStatusService.readAllReadStatus().stream().map(ApiResponseDto::success).toList();
//        return new ResponseEntity<List<ApiResponseDto<ReadStatusResponseDto>>>(apiResponseDtoList,HttpStatus.OK);
//    }
}
