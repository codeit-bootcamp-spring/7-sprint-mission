package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Optional;

@SpringBootApplication
public class DiscodeitApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiscodeitApplication.class, args);
    }

    /**
     * 컨트롤러 레이어 (요구사항: @RequestMapping만 사용)
     * - User 생성
     * - Channel 생성
     * - Message 생성
     * 전역 예외처리는 다음 단계에서 적용 예정.
     */
    @RestController
    @RequestMapping("/api/users")
    static class UserController {
        private final UserService userService;

        public UserController(UserService userService) {
            this.userService = userService;
        }

        // POST /api/users
        @RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
        public ResponseEntity<User> create(@RequestBody UserCreateRequest request) {
            User created = userService.create(request, Optional.empty());
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        }
    }

    @RestController
    @RequestMapping("/api/channels")
    static class ChannelController {
        private final ChannelService channelService;

        public ChannelController(ChannelService channelService) {
            this.channelService = channelService;
        }

        // POST /api/channels
        @RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
        public ResponseEntity<Channel> create(@RequestBody PublicChannelCreateRequest request) {
            Channel created = channelService.create(request);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        }
    }

    @RestController
    @RequestMapping("/api/messages")
    static class MessageController {
        private final MessageService messageService;

        public MessageController(MessageService messageService) {
            this.messageService = messageService;
        }

        // POST /api/messages
        @RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
        public ResponseEntity<Message> create(@RequestBody MessageCreateRequest request) {
            // 첨부파일은 추후 구현 예정 → 임시로 빈 리스트 처리
            Message created = messageService.create(request, new ArrayList<>());
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        }
    }
}
