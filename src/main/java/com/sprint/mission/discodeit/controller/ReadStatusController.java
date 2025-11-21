package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.readStatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.readStatus.ReadStatusPatchRequestDto;
import com.sprint.mission.discodeit.dto.response.readStatus.ReadStatusDto;
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

    @PostMapping( "")
    @Override
    public ResponseEntity<ReadStatusDto> createReadStatus(@Valid @RequestBody ReadStatusCreateRequestDto dto){

        return new ResponseEntity<ReadStatusDto>(readStatusService.createReadStatus(
                dto
        ), HttpStatus.CREATED);
    }

    @GetMapping("")
    @Override
    public ResponseEntity<List<ReadStatusDto>> readReadStatus(@RequestParam UUID userId){
        return new ResponseEntity<List<ReadStatusDto>>(readStatusService.findAllyByUserId(userId), HttpStatus.OK);
    }

    @PatchMapping("/{readStatusId}")
    @Override
    public ResponseEntity<ReadStatusDto> patchReadStatus(@PathVariable UUID readStatusId, @Valid @RequestBody ReadStatusPatchRequestDto dto)
    {
     return new ResponseEntity<>(readStatusService.patchReadStatus(readStatusId, dto), HttpStatus.OK);
    }

    @PostMapping( "/reset")
    public void resetReadStatus(){
        readStatusService.resetReadStatus();
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<ReadStatusDto>> readAll(){
        return new ResponseEntity<>(readStatusService.readAllReadStatus(),HttpStatus.OK);
    }

}
