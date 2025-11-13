package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.readStatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.readStatus.ReadStatusPatchRequestDto;
import com.sprint.mission.discodeit.dto.response.ReadStatusResponseDto;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/readStatuses")
@RequiredArgsConstructor
public class ReadStatusController implements ReadStatusControllerDocs {

    private final ReadStatusService readStatusService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    @Override
    public ResponseEntity<ReadStatusResponseDto> createReadStatus(@Valid @RequestBody ReadStatusCreateRequestDto dto){

        return new ResponseEntity<ReadStatusResponseDto>(readStatusService.createReadStatus(
                dto
        ), HttpStatus.CREATED);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @Override
    public ResponseEntity<List<ReadStatusResponseDto>> readReadStatus(@RequestParam UUID userId){
        return new ResponseEntity<List<ReadStatusResponseDto>>(readStatusService.findAllyByUserId(userId), HttpStatus.OK);
    }

    @RequestMapping(value = "/{readStatusId}", method = RequestMethod.PATCH)
    @Override
    public ResponseEntity<ReadStatusResponseDto> patchReadStatus(@PathVariable UUID readStatusId, @Valid @RequestBody ReadStatusPatchRequestDto dto)
    {
     return new ResponseEntity<>(readStatusService.patchReadStatus(readStatusId, dto), HttpStatus.OK);
    }

    @RequestMapping(value = "/reset",method = RequestMethod.GET)
    public void resetReadStatus(){
        readStatusService.resetReadStatus();
    }

    @RequestMapping(value = "",method = RequestMethod.OPTIONS)
    public ResponseEntity<List<ReadStatusResponseDto>> readAll(){
        return new ResponseEntity<>(readStatusService.readAllReadStatus(),HttpStatus.OK);
    }
    /// ////////////////////////////////////////////////////////////////////////////////////////////////
//
//    @RequestMapping(value = "/update", method = RequestMethod.POST)
//    public <T>void updateReadStatus(@RequestBody ReadStatusUpdateRequestDto<T> dto){
//        readStatusService.updateReadStatus(dto);
//    }

}
