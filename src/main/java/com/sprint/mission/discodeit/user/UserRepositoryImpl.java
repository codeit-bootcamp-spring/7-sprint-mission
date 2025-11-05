package com.sprint.mission.discodeit.user;

import com.sprint.mission.discodeit.common.repository.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserRepositoryImpl extends BaseRepositoryImpl<User, UUID> implements UserRepository {

    /**
     * {@inheritDoc}
     * <p>
     * <b>[성능 주의]</b> 이 메서드는 저장된 모든 사용자 데이터를 순회(Full Scan)하여
     * 사용자 이름이 일치하는 첫 번째 사용자를 찾습니다.
     * 시간 복잡도는 O(n)으로, 사용자 수가 많아지면 성능 저하의 원인이 될 수 있습니다.
     */
    @Override
    public Optional<User> findByUsername(String username) {
        return dataMap.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    /**
     * {@inheritDoc}
     * <p>
     * <b>[성능 주의]</b> 이 메서드는 저장된 모든 사용자 데이터를 순회(Full Scan)하여
     * 사용자 이름의 존재 여부를 확인합니다.
     * 시간 복잡도는 O(n)이며, {@code findFirst}보다 최적화되어 있지만 여전히 사용자 수에 비례하여 시간이 소요됩니다.
     */
    @Override
    public boolean existsByUsername(String username) {
        return dataMap.values().stream()
                .anyMatch(user -> user.getUsername().equals(username));
    }

    /**
     * {@inheritDoc}
     * <p>
     * <b>[성능 주의]</b> 모든 사용자 데이터를 순회(O(n))하여 논리적으로 삭제되지 않고
     * 사용자 이름이 일치하는 첫 번째 사용자를 찾습니다.
     */
    @Override
    public Optional<User> findByUsernameNonDel(String username) {
        return dataMap.values().stream()
                .filter(user -> !user.isDeleted() && user.getUsername().equals(username)) // 두 조건을 한 번에 필터링
                .findFirst();
    }

    /**
     * {@inheritDoc}
     * <p>
     * <b>[성능 주의]</b> 모든 사용자 데이터를 순회(O(n))하여 논리적으로 삭제되지 않고
     * 사용자 이름이 일치하는 사용자가 있는지 확인합니다.
     */
    @Override
    public boolean existsByUsernameNonDel(String username) {
        return dataMap.values().stream()
                .anyMatch(user -> !user.isDeleted() && user.getUsername().equals(username)); // 두 조건을 한 번에 확인
    }

    @Override
    public boolean existsByUserNickName(String nickName) {
        return dataMap.values().stream()
                .anyMatch(user -> user.getNickname().equals(nickName));
    }
}