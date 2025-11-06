package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.Binarycontent.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.Binarycontent.request.BinaryContentFindByIdRequest;
import com.sprint.mission.discodeit.dto.Binarycontent.response.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.content.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContent")
@RequiredArgsConstructor
public class BinaryContentController {

    private final BinaryContentService binaryContentService;
    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public ResponseEntity<BinaryContent> findBinaryContent(
            @RequestParam UUID binaryContentId){
        System.out.println(binaryContentId);
        BinaryContentResponse findBinary = binaryContentService.find(binaryContentId);

        byte[] bytes = binaryContentService.find(binaryContentId).contentByte();

        //이건 화면에 보이게 하려고 억지로 넣었다
        String encodedString = Base64.getEncoder().encodeToString(bytes);
        //
        BinaryContent binaryContent = new BinaryContent(findBinary.contentsType(),bytes, encodedString );

        return ResponseEntity.ok(binaryContent);
    }
}
