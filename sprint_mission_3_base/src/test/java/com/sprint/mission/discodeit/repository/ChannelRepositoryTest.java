package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.config.TestJpaAuditingConfig;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Import(TestJpaAuditingConfig.class)
class ChannelRepositoryTest {

    @Autowired
    private ChannelRepository channelRepository;

    @Test
    @DisplayName("findAllByTypeOrIdIn 실패: ids가 비어있고 PUBLIC도 없으면 빈 리스트를 반환한다")
    void findAllByTypeOrIdIn_emptyIdsAndNoPublic_returnsEmpty() {

        Channel private1 = Channel.builder()
                .name("private-1")
                .description("p1")
                .type(ChannelType.PRIVATE)
                .build();

        Channel private2 = Channel.builder()
                .name("private-2")
                .description("p2")
                .type(ChannelType.PRIVATE)
                .build();

        channelRepository.saveAll(List.of(private1, private2));

        List<Channel> result = channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, List.of());


        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("findAllByTypeOrIdIn 성공: PUBLIC 채널 + ids에 포함된 채널을 함께 조회한다")
    void findAllByTypeOrIdIn_publicPlusIds_returnsPublicAndIncluded() {

        Channel public1 = channelRepository.save(Channel.builder()
                .name("public-1")
                .description("u1")
                .type(ChannelType.PUBLIC)
                .build());

        Channel public2 = channelRepository.save(Channel.builder()
                .name("public-2")
                .description("u2")
                .type(ChannelType.PUBLIC)
                .build());

        Channel private1 = channelRepository.save(Channel.builder()
                .name("private-1")
                .description("p1")
                .type(ChannelType.PRIVATE)
                .build());

        Channel private2 = channelRepository.save(Channel.builder()
                .name("private-2")
                .description("p2")
                .type(ChannelType.PRIVATE)
                .build());

        List<UUID> ids = List.of(private2.getId());

        List<Channel> result = channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, ids);

        assertThat(result)
                .extracting(Channel::getId)
                .contains(public1.getId(), public2.getId(), private2.getId())
                .doesNotContain(private1.getId());
    }

    @Test
    @DisplayName("페이징/정렬 성공: name 내림차순으로 2개씩 조회된다")
    void pagingAndSorting_success() {

        channelRepository.save(Channel.builder().name("A").description("A").type(ChannelType.PUBLIC).build());
        channelRepository.save(Channel.builder().name("B").description("B").type(ChannelType.PUBLIC).build());
        channelRepository.save(Channel.builder().name("C").description("C").type(ChannelType.PUBLIC).build());

        PageRequest pageRequest = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "name"));
        Page<Channel> page = channelRepository.findAll(pageRequest);

        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getContent().get(0).getName()).isEqualTo("C");
        assertThat(page.getContent().get(1).getName()).isEqualTo("B");
    }

    @Test
    @DisplayName("페이징/정렬 실패: 범위를 벗어난 페이지는 빈 결과를 반환한다")
    void pagingAndSorting_outOfRange_returnsEmpty() {

        channelRepository.save(Channel.builder().name("A").description("A").type(ChannelType.PUBLIC).build());
        channelRepository.save(Channel.builder().name("B").description("B").type(ChannelType.PUBLIC).build());
        channelRepository.save(Channel.builder().name("C").description("C").type(ChannelType.PUBLIC).build());

        PageRequest pageRequest = PageRequest.of(2, 2, Sort.by(Sort.Direction.DESC, "name"));
        Page<Channel> page = channelRepository.findAll(pageRequest);

        assertThat(page.getContent()).isEmpty();
    }
}
