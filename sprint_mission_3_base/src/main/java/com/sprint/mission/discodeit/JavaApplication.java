package com.sprint.mission.discodeit;
/*
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;

import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;

import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
// JCF 구현은 이미 있으시면 그대로 사용
import com.sprint.mission.discodeit.repository.jcf.JCFReadStatusRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserStatusRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFBinaryContentRepository;

import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;

import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;

public class JavaApplication {

    public static void main(String[] args) {
        // ---------- 레포지토리 초기화 ----------
        UserRepository userRepository = new FileUserRepository();
        ChannelRepository channelRepository = new FileChannelRepository();
        MessageRepository messageRepository = new FileMessageRepository();

        // 읽음/상태/바이너리(JCF 간단 구현 사용 예)
        ReadStatusRepository readStatusRepository = new JCFReadStatusRepository();
        UserStatusRepository userStatusRepository = new JCFUserStatusRepository();
        BinaryContentRepository binaryContentRepository = new JCFBinaryContentRepository();

        // ---------- 서비스 초기화 (생성자 시그니처 맞추기) ----------
        UserService userService = new BasicUserService(
                userRepository, userStatusRepository, binaryContentRepository
        );
        ChannelService channelService = new BasicChannelService(
                channelRepository, messageRepository, readStatusRepository, userRepository
        );
        MessageService messageService = new BasicMessageService(
                messageRepository, channelRepository, userRepository, binaryContentRepository
        );

        // ---------- 1) 유저 생성 (DTO) ----------
        UserDto user = userService.create(
                new UserCreateRequest("woody", "woody@codeit.com", "woody1234", null)
        );
        System.out.println("[USER] id=" + user.id() + ", username=" + user.username());

        // ---------- 2) 공개 채널 생성 (DTO) ----------
        ChannelDto channel = channelService.createPublic(
                new CreatePublicChannelRequest("공지", "공지 채널입니다.")
        );
        System.out.println("[CHANNEL] id=" + channel.id() + ", name=" + channel.name());

        // ---------- 3) 메시지 생성 (DTO) ----------
        MessageDto message = messageService.create(
                new MessageCreateRequest(channel.id(), user.id(), "안녕하세요.",null)
        );
        System.out.println("[MESSAGE] id=" + message.id() + ", content=" + message.content());
    }
}*/
