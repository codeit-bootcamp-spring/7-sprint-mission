package com.sprint.mission.discodeit.integration;

import com.sprint.mission.discodeit.dto.channelDto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channelDto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channelDto.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.role.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ChannelIntegrationTest extends IntegrationTest {

    @Autowired
    private ChannelRepository channelRepository;

    @Test
    @DisplayName("Public 채널을 생성할 수 있다.")
    void createPublicChannel() throws Exception {
        // given
        PublicChannelCreateRequest request = new PublicChannelCreateRequest("test", "공지사항");

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/channels/public")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        // when & then
        Channel savedChannel = channelRepository.findAll().get(0);
        assertThat(savedChannel.getName()).isEqualTo("test");
        assertThat(savedChannel.getDescription()).isEqualTo("공지사항");
    }
    
    @Test
    @DisplayName("Private 채널을 생성할 수 있다.")
    void createPrivateChannel() throws Exception {
        // given
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();

        List<UUID> userIds = List.of(userId1, userId2);

        PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(userIds);

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/channels/private")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        // when & then
        Channel savedChannel = channelRepository.findAll().get(0);
        assertThat(savedChannel.getType()).isEqualTo(ChannelType.PRIVATE);
    }

    @Test
    @DisplayName("Public 채널의 정보를 수정할 수 있다.")
    void updatePublicChannel() throws Exception {
        // given
        Channel channel = new  Channel("oldName", "oldDescription", ChannelType.PUBLIC);
        channelRepository.save(channel);

        PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("newName",  "newDescription");
        String json = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(patch("/api/channels/" +  channel.getId())
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // then
        Channel savedChannel = channelRepository.findAll().get(0);
        assertThat(savedChannel.getName()).isEqualTo("newName");
        assertThat(savedChannel.getDescription()).isEqualTo("newDescription");
    }

    @Test
    @DisplayName("채널을 삭제할 수 있다.")
    void deleteChannel() throws Exception {
        // given
        Channel channel = new Channel(ChannelType.PUBLIC);
        channelRepository.save(channel);

        mockMvc.perform(delete("/api/channels/" + channel.getId()))
                .andExpect(status().isNoContent());
        assertThat(channelRepository.findById(channel.getId())).isEmpty();
        // when


        // then


    }
}
