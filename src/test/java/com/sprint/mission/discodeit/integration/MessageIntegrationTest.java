package com.sprint.mission.discodeit.integration;

import com.sprint.mission.discodeit.dto.messageDto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.messageDto.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.entity.role.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MessageIntegrationTest extends IntegrationTest {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChannelRepository channelRepository;

    @Test
    @DisplayName("메시지를 생성할 수 있다.")
    @Transactional
    void createMessage() throws Exception {
        // given
        User user = new User("test@codeit.com", "Password1!", "test");
        UserStatus userStatus = new UserStatus(user);
        user.updateStatus(userStatus);
        userRepository.save(user);
        Channel channel
                = channelRepository.save(new Channel(ChannelType.PUBLIC));

        MessageCreateRequest request = MessageCreateRequest.builder()
                .content("hello")
                .authorId(user.getId())
                .channelId(channel.getId())
                .build();

        String json = objectMapper.writeValueAsString(request);
        MockMultipartFile multipartFile
                = new MockMultipartFile("messageCreateRequest", "",
                MediaType.APPLICATION_JSON_VALUE, json.getBytes());

        // when
        mockMvc.perform(multipart("/api/messages")
                .file(multipartFile))
                .andExpect(status().isCreated());

        // then
        assertThat(messageRepository.findAllByChannelId(
                channel.getId(),
                Instant.now().plusSeconds(10),
                PageRequest.of(0,10)).get(0).getContent())
                .isEqualTo("hello");
    }

    @Test
    @DisplayName("메시지를 수정할 수 있다.")
    @Transactional
    void updateMessage() throws Exception {
        // given
        User user = new User("test@codeit.com", "Password1!", "test");
        UserStatus userStatus = new UserStatus(user);
        user.updateStatus(userStatus);
        userRepository.save(user);

        Channel channel
                = channelRepository.save(new Channel(ChannelType.PUBLIC));
        channelRepository.save(channel);

        Message message = new Message("oldContent", channel, user, new ArrayList<>());
        messageRepository.save(message);

        MessageUpdateRequest request = MessageUpdateRequest.builder()
                .newContent("newContent")
                .build();
        String json = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(patch("/api/messages/" + message.getId())
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // then
        assertThat(messageRepository.findAllByChannelId(
                channel.getId(),
                Instant.now().plusSeconds(10),
                PageRequest.of(0,10)).get(0).getContent())
                .isEqualTo("newContent");

    }
}
