//package com.sprint.mission.discodeit.entity;
//
//import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.OneToOne;
//import jakarta.persistence.Table;
//import java.time.Duration;
//import java.time.Instant;
//import lombok.AccessLevel;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@Entity
//@Table(name = "user_statuses")
//@Getter @Setter
//@NoArgsConstructor(access = AccessLevel.PROTECTED) // 🔥 추가 필수
//@AllArgsConstructor
//public class UserStatus extends BaseUpdatableEntity {
////    사용자 별 마지막으로 확인된 접속 시간을 표현하는 도메인 모델입니다. 사용자의 온라인 상태를 확인하기 위해 활용합니다.
//
//    // FK 갖고 있는 테이블이 연관관계의 주인이다!!! - 주인 아닌 도메인 모델 @OneToOne(mappedB)
//    @OneToOne
//    @JoinColumn(name = "user_id", nullable = false , unique = true)
//    private User user;
//
//    @Column(name = "last_active_at", nullable = false)
//    private Instant lastActiveAt;
//
////    public UserStatus(User user, Instant lastActiveAt) {
////        this.user = user;
////        this.lastActiveAt = lastActiveAt;
////    }
//
//    public boolean isOnline() {
//        //[ ] 마지막 접속 시간을 기준으로 현재 로그인한 유저로 판단할 수 있는 메소드를 정의하세요.
//        // 마지막 접속 시간이 현재 시간으로부터 5분 이내이면 현재 접속 중인 유저로 간주합니다.
//        Duration duration = Duration.between(lastActiveAt, Instant.now());
//        return duration.toMinutes() < 5;
//    }
//
//    // 연관관계 편의 메서드(양방향일 경우. 필요하면)
//    public void changeUser(User user) {
//        this.user = user;
//        user.setUserStatus(this);
//    }
//
//    @Override
//    public String toString() {
//        return "✅ UserStatus{" +
//            super.toString() +
//            "user=" + user.getId() + "/ " + user.getUsername() +
//            ", lastActiveAt=" + lastActiveAt +
//            '}';
//    }
//}
