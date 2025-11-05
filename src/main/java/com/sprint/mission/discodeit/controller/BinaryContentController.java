package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.response.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binary-contents")
@RequiredArgsConstructor
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    @RequestMapping(method = RequestMethod.POST)
    public BinaryContentResponseDto create(
            @Valid @RequestBody BinaryContentCreateRequestDto binaryContentCreateRequestDto) {
        BinaryContent binaryContent = binaryContentService.create(binaryContentCreateRequestDto);
        return BinaryContentResponseDto.from(binaryContent);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public BinaryContentResponseDto get(@PathVariable("id") UUID id) {
        BinaryContent binaryContent = binaryContentService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("binaryContent not found"));
        return BinaryContentResponseDto.from(binaryContent);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<BinaryContentResponseDto> getAllById(@RequestParam("ids") List<UUID> ids) {
        List<BinaryContent> list = binaryContentService.findAllByIdIn(ids);
        return list.stream()
                .map(bc -> BinaryContentResponseDto.from(bc))
                .toList();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") UUID id) {
        binaryContentService.delete(id);
    }
}
