package com.sprint.mission.discodeit.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.message.request.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.response.MessageResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.enums.ChannelType;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("Message 통합 테스트")
public class MessageIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private BinaryContentRepository binaryContentRepository;

    @Nested
    @DisplayName("메세지 생성 테스트")
    class MessageCreate {
        @Test
        @DisplayName("[정상 케이스] - 메세지 생성 성공")
        void createMessage_success() throws Exception {
            // given
            User user1 = new User("test1", "test@codeit.com", "test_123", null);
            User user2 = new User("test2", "test2@codeit.com", "test_456", null);
            userRepository.saveAll(List.of(user1, user2));
            Channel channel = new Channel(ChannelType.PUBLIC, "test room", "test description");
            channelRepository.save(channel);
            UUID user1Id = user1.getId();
            UUID channelId = channel.getId();

            CreateMessageDto createMessageDto = new CreateMessageDto("test content", user1Id, channelId);
            MockMultipartFile messageRequest = new MockMultipartFile(
                    "messageCreateRequest",
                    "",
                    MediaType.APPLICATION_JSON_VALUE,
                    objectMapper.writeValueAsBytes(createMessageDto)

            );
            MockMultipartFile attachment1 = new MockMultipartFile(
                    "attachments",
                    "attachments.jpg",
                    MediaType.IMAGE_JPEG_VALUE,
                    "attachments".getBytes()
            );
            MockMultipartFile attachment2 = new MockMultipartFile(
                    "attachments",
                    "attachments2.jpg",
                    MediaType.IMAGE_JPEG_VALUE,
                    "attachments2".getBytes()
            );
            // when
            MvcResult mvcResult = mockMvc.perform(multipart("/api/messages")
                            .file(messageRequest)
                            .file(attachment1)
                            .file(attachment2)
                            .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.content").value("test content"))
                    .andReturn();

            // then
            String responseBody = mvcResult.getResponse().getContentAsString();
            MessageResponseDto messageResponseDto = objectMapper.readValue(responseBody, MessageResponseDto.class);

            Optional<Message> message = messageRepository.findById(messageResponseDto.id());

            assertThat(message).isPresent();
            assertThat(message.get().getContent()).isEqualTo("test content");

            List<BinaryContent> binaryContentList = binaryContentRepository.findAll();
            assertThat(binaryContentList).hasSize(2);
        }

        @Test
        @DisplayName("[예외 케이스] - 메세지 생성 실패 (존재하지 않는 user)")
        void createMessage_notFoundUser_fail() throws Exception {
            // given
            User user1 = new User("test1", "test@codeit.com", "test_123", null);
            User user2 = new User("test2", "test2@codeit.com", "test_456", null);
            userRepository.saveAll(List.of(user1, user2));
            Channel channel = new Channel(ChannelType.PUBLIC, "test room", "test description");
            channelRepository.save(channel);
            UUID user1Id = UUID.randomUUID();
            UUID channelId = channel.getId();

            CreateMessageDto createMessageDto = new CreateMessageDto("test content", user1Id, channelId);
            MockMultipartFile messageRequest = new MockMultipartFile(
                    "messageCreateRequest",
                    "",
                    MediaType.APPLICATION_JSON_VALUE,
                    objectMapper.writeValueAsBytes(createMessageDto)

            );
            MockMultipartFile attachment1 = new MockMultipartFile(
                    "attachments",
                    "attachments.jpg",
                    MediaType.IMAGE_JPEG_VALUE,
                    "attachments".getBytes()
            );
            MockMultipartFile attachment2 = new MockMultipartFile(
                    "attachments",
                    "attachments2.jpg",
                    MediaType.IMAGE_JPEG_VALUE,
                    "attachments2".getBytes()
            );
            // when
            mockMvc.perform(multipart("/api/messages")
                            .file(messageRequest)
                            .file(attachment1)
                            .file(attachment2)
                            .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("USER_NOT_FOUND"));
        }
    }

    @Nested
    @DisplayName("메세지 삭제 테스트")
    class MessageDelete {
        @Test
        @DisplayName("[정상 케이스] - 메세지 삭제 성공")
        void deleteMessage_success() throws Exception {
            // given
            User user1 = new User("test1", "test@codeit.com", "test_123", null);
            User user2 = new User("test2", "test2@codeit.com", "test_456", null);
            userRepository.saveAll(List.of(user1, user2));
            Channel channel = new Channel(ChannelType.PUBLIC, "test room", "test description");
            channelRepository.save(channel);
            Message message = new Message("test content", channel, user1, null);
            messageRepository.save(message);
            UUID messageId = message.getId();
            // when

            mockMvc.perform(delete("/api/messages/{messageId}", messageId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            // then
            assertThat(messageRepository.findById(messageId)).isEmpty();
        }

        @Test
        @DisplayName("[예외 케이스] - 메세지 삭제 실패 - 존재하지 않는 메세지 UUID")
        void deleteMessage_notFoundChannel_fail() throws Exception {
            // given
            User user1 = new User("test1", "test@codeit.com", "test_123", null);
            User user2 = new User("test2", "test2@codeit.com", "test_456", null);
            userRepository.saveAll(List.of(user1, user2));
            Channel channel = new Channel(ChannelType.PUBLIC, "test room", "test description");
            channelRepository.save(channel);
            Message message = new Message("test content", channel, user1, null);
            messageRepository.save(message);
            UUID messageId = message.getId();
            UUID notFoundMessageId = UUID.randomUUID();

            // when

            mockMvc.perform(delete("/api/messages/{messageId}", notFoundMessageId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("MESSAGE_NOT_FOUND"));

            // then
            assertThat(messageRepository.findById(messageId)).isPresent();
        }
    }
}
