package com.sprint.mission.discodeit.integration;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.dto.request.MessageCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MessageIntegrationTest extends IntegrationTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ChannelRepository channelRepository;
    @Autowired
    ReadStatusRepository readStatusRepository;
    @Autowired
    MessageRepository messageRepository;

    @Nested
    @DisplayName("메세지 생성")
    class CreateMessage {
        @Test
        @DisplayName("메세지 생성 성공")
        void createMessage_success() throws Exception {
            //given
            User user = new User("test@email.com", "1234", "test");
            User save = userRepository.save(user);
            Channel channel = new Channel("test", "description", ChannelType.PUBLIC);
            Channel saveChannel = channelRepository.save(channel);
            ReadStatus readStatus = new ReadStatus(user, channel);
            readStatusRepository.save(readStatus);

            MessageCreateRequest messageCreateRequest = new MessageCreateRequest("content", save.getId(), saveChannel.getId());

            MockMultipartFile file = new MockMultipartFile(
                    "attachments",
                    "file1.txt",
                    MediaType.IMAGE_PNG_VALUE,
                    "fake file".getBytes()
            );
            MockMultipartFile file2 = new MockMultipartFile(
                    "attachments",
                    "file2.txt",
                    MediaType.IMAGE_PNG_VALUE,
                    "fake file".getBytes()
            );


            MockMultipartFile requestPart = new MockMultipartFile(
                    "messageCreateRequest",
                    "",
                    MediaType.APPLICATION_JSON_VALUE,
                    objectMapper.writeValueAsBytes(messageCreateRequest)
            );


            //when, then
            mockMvc.perform(multipart("/api/messages")
                            .file(requestPart)
                            .file(file)
                            .file(file2)
                            .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.author.id").value(save.getId().toString()))
                    .andExpect(jsonPath("$.channelId").value(saveChannel.getId().toString()));
        }

        @Test
        @DisplayName("메세지 생성 실패 - messageCreateRequest 누락")
        void createMessage_fail_noMessageCreateRequest() throws Exception {
            // when, then
            mockMvc.perform(multipart("/api/messages")
                            .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("메세지 수정")
    class UpdateMessage {
        @Test
        @DisplayName("메세지 수정 성공")
        void updateMessage_success() throws Exception {
            // given
            User user = new User("test@email.com", "1234", "test");
            User save = userRepository.save(user);
            Channel channel = new Channel("test", "description", ChannelType.PUBLIC);
            Channel saveChannel = channelRepository.save(channel);
            Message message = new Message(save, saveChannel, "test");
            Message saveMessage = messageRepository.save(message);

            // when, then
            mockMvc.perform(
                            multipart("/api/messages/{messageId}", saveMessage.getId())
                                    .with(requestBuilder -> {
                                        requestBuilder.setMethod("PATCH");
                                        return requestBuilder;
                                    })
                                    .param("newContent", "updated content")
                                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    )
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.content").value("updated content"));
        }

        @Test
        @DisplayName("메세지 수정 실패 - 존재하지 않는 messageId")
        void updateMessage_fail() throws Exception {
            // given
            UUID messageId = UUID.randomUUID();

            // when, then
            mockMvc.perform(
                            multipart("/api/messages/{messageId}", messageId)
                                    .with(request -> {
                                        request.setMethod("PATCH");
                                        return request;
                                    })
                                    .param("newContent", "updated content")
                                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("메세지 삭제")
    class DeleteMessage {
        @Test
        @DisplayName("메세지 삭제 성공")
        void deleteMessage_success() throws Exception {
            // given
            User user = new User("test@email.com", "1234", "test");
            User save = userRepository.save(user);
            Channel channel = new Channel("test", "description", ChannelType.PUBLIC);
            Channel saveChannel = channelRepository.save(channel);
            Message message = new Message(save, saveChannel, "test");
            Message saveMessage = messageRepository.save(message);

            // when, then
            mockMvc.perform(delete("/api/messages/{messageId}", saveMessage.getId()))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("메세지 삭제 실패")
        void deleteMessage_fail() throws Exception {
            // when, then
            mockMvc.perform(delete("/api/messages/{messageId}", "no-uuid"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("메세지 조회")
    class GetMessage {
        @Test
        @DisplayName("채널별 메세지 조회 성공")
        void getAllMessageByChannelId_success() throws Exception {
            // given
            User user = new User("test@email.com", "1234", "test");
            User save = userRepository.save(user);
            Channel channel = new Channel("test", "description", ChannelType.PUBLIC);
            Channel saveChannel = channelRepository.save(channel);
            Message message = new Message(save, saveChannel, "test1");
            Message message2 = new Message(save, saveChannel, "test2");
            Message message3 = new Message(save, saveChannel, "test3");
            Message saveMessage = messageRepository.save(message);
            Message saveMessage2 = messageRepository.save(message2);
            Message saveMessage3 = messageRepository.save(message3);

            int size = 50;
            String sort = "createdAt,desc";

            // when, then
            mockMvc.perform(get("/api/messages")
                            .param("channelId", saveChannel.getId().toString())
                            .param("size", String.valueOf(size))
                            .param("sort", sort)
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", hasSize(3)))
                    .andExpect(jsonPath("$.content[0].content").value("test3"))
                    .andExpect(jsonPath("$.content[1].content").value("test2"))
                    .andExpect(jsonPath("$.size").value(3))
                    .andExpect(jsonPath("$.hasNext").value(false));

        }

        @Test
        @DisplayName("채널별 메세지 조회 실패 - channelId 누락")
        void getAllMessageByChannelId_fail_missingChannelId() throws Exception {
            // when, then
            mockMvc.perform(get("/api/messages")
                            .param("size", "50")
                            .param("sort", "DESC")
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest());

        }
    }
}
