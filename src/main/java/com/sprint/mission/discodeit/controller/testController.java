package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.BasicUserService;
import com.sprint.mission.discodeit.application.FileService;
import com.sprint.mission.discodeit.application.dto.response.UserDto;
import com.sprint.mission.discodeit.domain.BinaryContent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class testController {
    //심화 요구 사항을 위해 만든 컨트롤러 입니다.
    //이 컨트롤러를 통해 화면에 필요한 데이터를 반환합니다

    private final BasicUserService userService;
    private final FileService fileService;

    @GetMapping("/api/user/findAll")
    public ResponseEntity<List<UserDto>> findAllUsers(){
        List<UserDto> list = userService.findAll().stream().map(UserDto::from).toList();
        return ResponseEntity.ok()
                .body(list);
    }

    @GetMapping("/api/binaryContent/find")
    public ResponseEntity<BinaryContent> getFile(@PathVariable UUID id) throws MalformedURLException {
        BinaryContent content = fileService.findById(id);
        return ResponseEntity.ok()
                .body(content);
    }
}
