package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.binary.*;
import com.sprint.mission.discodeit.service.BinaryContentService;
import org.springframework.web.bind.annotation.*;
import java.util.*;


@RestController
@RequestMapping("/api/binaries")
public class BinaryContentController {


    private final BinaryContentService binaryContentService;


    public BinaryContentController(BinaryContentService binaryContentService) {
        this.binaryContentService = binaryContentService;
    }


    @RequestMapping(method = RequestMethod.POST)
    public BinaryContentDto create(@RequestBody BinaryContentCreateRequest request) {
        return binaryContentService.create(request);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public BinaryContentDto find(@PathVariable UUID id) {
        return binaryContentService.find(id);
    }


    @RequestMapping(value = "/bulk", method = RequestMethod.POST)
    public List<BinaryContentDto> findAllByIds(@RequestBody List<UUID> ids) {
        return binaryContentService.findAllByIdIn(ids);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable UUID id) {
        binaryContentService.delete(id);
    }
}