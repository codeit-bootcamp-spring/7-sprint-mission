package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final BinaryContentService binaryContentService;
    @RequestMapping(value = "/read", method = RequestMethod.POST)
    public List<BinaryContent> read(@RequestBody List<UUID> binaryContentIdList){
        return binaryContentService.findAllByIdIn(binaryContentIdList);
    }

    @RequestMapping("/readAll")
    public List<BinaryContent> readAll(){
        return binaryContentService.findAll();
    }

    @RequestMapping("/reset")
    public void reset(){
        binaryContentService.resetBinaryContentService();
    }
}
