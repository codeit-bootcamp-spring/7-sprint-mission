package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 메모리(JCF) 기반으로 ChannelRepository 인터페이스를 구현한 클래스입니다.
 */
public class JCFChannelRepository extends JCFBaseRepository<Channel, UUID> implements ChannelRepository {

    /**
     * {@inheritDoc}
     * <p>
     * <b>[성능 주의]</b> 이 메서드는 저장된 모든 채널 데이터를 순회(Full Scan)하여
     * 이름이 일치하는 첫 번째 채널을 찾습니다.
     * 시간 복잡도는 O(n)으로, 채널 수가 많아지면 성능 저하의 원인이 될 수 있습니다.
     */
    @Override
    public Optional<Channel> findByChannelName(String name) {
        // dataMap에 있는 모든 Channel 객체를 순회합니다.
        return dataMap.values().stream()
                // stream의 filter를 사용하여 이름이 일치하는 채널을 찾습니다.
                .filter(channel -> channel.getChannelName().equals(name))
                // 가장 먼저 찾은 1개를 반환합니다.
                .findFirst();
    }

    /**
     * {@inheritDoc}
     * <p>
     * <b>[성능 주의]</b> 이 메서드는 저장된 모든 채널 데이터를 순회(Full Scan)하여
     * 채널 이름의 존재 여부를 확인합니다.
     * 시간 복잡도는 O(n)이며, {@code findFirst}보다 최적화되어 있지만 여전히 채널 수에 비례하여 시간이 소요됩니다.
     */
    @Override
    public boolean existsByChannelName(String name) {
        // dataMap에 있는 모든 Channel 객체를 순회하며,
        // anyMatch를 사용하여 이름이 일치하는 것이 하나라도 있는지 확인합니다.
        return dataMap.values().stream()
                .anyMatch(channel -> channel.getChannelName().equals(name));
    }

    /**
     * {@inheritDoc}
     * <p>
     * 이 구현체는 메모리에 있는 모든 채널 데이터를 스트림으로 변환하고,
     * null이 아닌 검색 조건들을 순차적으로 적용하여 결과를 필터링합니다.
     */
    @Override
    public List<Channel> findAllChannelsBySettings(String channelName, ChannelType channelType, String topic) {
        // 1. 저장소의 모든 채널 데이터를 스트림으로 변환합니다.
        Stream<Channel> channelStream = findAll().stream();

        // 2. [조건부 필터링] channelName 파라미터가 유효한 경우, 이름에 해당 문자열이 포함된 채널만 필터링합니다.
        if (channelName != null && !channelName.isBlank()) {
            channelStream = channelStream.filter(channel ->
                    // 대소문자 구분 없이 검색하기 위해 모두 소문자로 변경하여 비교합니다.
                    channel.getChannelName().toLowerCase().contains(channelName.toLowerCase())
            );
        }

        // 3. [조건부 필터링] channelType 파라미터가 null이 아닌 경우, 타입이 일치하는 채널만 필터링합니다.
        if (channelType != null) {
            channelStream = channelStream.filter(channel -> channel.getChannelType() == channelType);
        }

        // 4. [조건부 필터링] topic 파라미터가 유효한 경우, 주제에 해당 문자열이 포함된 채널만 필터링합니다.
        if (topic != null && !topic.isBlank()) {
            channelStream = channelStream.filter(channel ->
                    // 채널에 따라 주제(topic)가 null일 수 있으므로, null 체크를 먼저 수행합니다.
                    channel.getTopic() != null &&
                            channel.getTopic().toLowerCase().contains(topic.toLowerCase())
            );
        }

        // 5. 모든 필터링을 거친 최종 결과를 List 형태로 수집하여 반환합니다.
        return channelStream.collect(Collectors.toList());
    }
}