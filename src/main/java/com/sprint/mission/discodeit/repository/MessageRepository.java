package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    @Query("""
             SELECT m FROM Message m
             WHERE m.channel.id = :channelId
             ORDER BY m.createdAt DESC
            """)
    List<Message> findLatestByChannelId(UUID channelId, Pageable pageable);

    @Query("""
                SELECT m.id FROM Message m where m.channel.id = :channelId
            """)
    List<UUID> findIdsByChannelId(UUID channelId);

    /**
     * NOTE: attachments에 대한 join fetch는 뺏음.
     * 1. OneToMany로 인해 message에 대한 거를 가져오는게아닌 중복되는 message+attachment도 같이 가져오기때문
     * 2. pagination 사용시 JPA가 원래면 offset, limit 을 쿼리문에 붙여주지만 그렇게 될시 실제 DB에서는 중복된 메세지(attachment가 여러개일수있으니)중에서 offet,limit을 가져오는거기때문에
     *    그것을 방지하고자 JPA가 빼고 쿼리문을 보내게 된다 따라서 모든 message와 관련된 join fetch를 가져온후 jpa가 인메모리에서 자체적으로 Pageable 정보를 토대로 걸러서 message와 연관된 필드들에 매칭을 해주는것이다
     *    즉, 모든 데이터를 불러온다라는 불필요한 요청이있고 그 요청으로인해 OOM이 발생할수있기 떄문에 소규모 데이터는 상관없지만 실무라면 이렇게 사용하면 안된다.
     * 3. 따라서 join fetch에서 OneToMany 테이블은 빼버리고 batch size를 사용하는걸로 택한다.
     *
     * NOTE: author, userStatus 는 NN(Not Null) 이기떄문에 left join fetch가 아닌 join fetch 사용
     * 1. 만약 inner join시 author 혹은 userStatus가 nullable하다면 조인과정에서 message가 필터링되서 결과에 포함되지 않는 문제가있다. 하지만 NN인걸 알기에 join fetch 로 inner join을 적용
     * 2. 만약 NN이 아니지만 결과에 포함하고자한다면 inner join이아닌 outer 조인으로 쓰고 명시적으로 left join fetch 라고 써준다.
     * NOTE: profile은 nullable하기때문에 left join fetch 사용
     */
    @Query("""
        select m
        from Message m
        join fetch m.author a
        join fetch a.userStatus
        left join fetch a.profile
        where m.channel.id = :channelId and m.createdAt < :cursor
""")
    Slice<Message> findAllByChannelId(UUID channelId, Pageable pageable,@Param("cursor") Instant cursor);

    Optional<Message> findByIdAndChannelId(UUID messageId, UUID channelId);


    void deleteByChannelId(UUID channelId);
}
