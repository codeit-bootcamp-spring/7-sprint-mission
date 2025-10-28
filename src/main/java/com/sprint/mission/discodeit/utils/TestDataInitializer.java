package com.sprint.mission.discodeit.utils;

import com.sprint.mission.discodeit.dto.request.CreateUserRequestDto;
import com.sprint.mission.discodeit.dto.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReceiveType;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

public class TestDataInitializer {
    public static void initialize(UserService userService, ChannelService channelService, MessageService messageService) {
        // 계정 생성하기 -> 메시지 생성 위함
        userService.createUser(new CreateUserRequestDto("테스트", "test", "test@naver.com", "010-1234-5678", "test1234", "test1234", null));
        userService.createUser(new CreateUserRequestDto("박지훈", "jihun", "jihun01@gmail.com", "010-1234-5678", "idjihun", "securePass1!", null));
        userService.createUser(new CreateUserRequestDto("이수빈", "subin", "subin09@naver.com", "010-8765-4321", "idsubin", "myPassword2@", null));
        userService.createUser(new CreateUserRequestDto("최하늘", "haneul", "haneul77@kakao.com", "010-3344-5566", "idhaneul", "skyPass123!", null));
        userService.createUser(new CreateUserRequestDto("이영희", "younghee", "younghee02@gmail.com", "010-3333-4444", "idyounghee", "pw567890", null));
        userService.createUser(new CreateUserRequestDto("박민수", "minsu", "minsu03@daum.net", "010-5555-6666", "idminsu", "securepw1", null));
        userService.createUser(new CreateUserRequestDto("최지현", "jihyun", "jihyun04@naver.com", "010-7777-8888", "idjihyun", "mypassword2", null));
        userService.createUser(new CreateUserRequestDto("오세훈", "sehun", "sehun05@kakao.com", "010-9999-0000", "idsehun", "hello4321", null));

        UserResponseDto discordItUser1 = userService.getUserByEmail("test@naver.com");
        UserResponseDto discordItUser2 = userService.getUserByEmail("jihun01@gmail.com");
        UserResponseDto discordItUser3 = userService.getUserByEmail("subin09@naver.com");
        UserResponseDto discordItUser4 = userService.getUserByEmail("haneul77@kakao.com");
        UserResponseDto discordItUser5 = userService.getUserByEmail("younghee02@gmail.com");
        UserResponseDto discordItUser6 = userService.getUserByEmail("minsu03@daum.net");
        UserResponseDto discordItUser7 = userService.getUserByEmail("jihyun04@naver.com");
        UserResponseDto discordItUser8 = userService.getUserByEmail("sehun05@kakao.com");

        // 메시지 생성
        // (1) discordItUser1 ↔ discordItUser2
        messageService.createMessage(discordItUser1.getId(), discordItUser2.getId(), "안녕하세요, 잘 지내시죠?", ReceiveType.USER);
        messageService.createMessage(discordItUser2.getId(), discordItUser1.getId(), "네! 오랜만이에요.", ReceiveType.USER);
        messageService.createMessage(discordItUser1.getId(), discordItUser2.getId(), "이번 주말에 시간 괜찮으세요?", ReceiveType.USER);
        messageService.createMessage(discordItUser2.getId(), discordItUser1.getId(), "네, 좋아요. 같이 영화 볼까요?", ReceiveType.USER);

        // (2) discordItUser3 ↔ discordItUser4
        messageService.createMessage(discordItUser3.getId(), discordItUser4.getId(), "오늘 회의 준비는 잘 하고 있어?", ReceiveType.USER);
        messageService.createMessage(discordItUser4.getId(), discordItUser3.getId(), "응, 거의 다 끝났어. 너는?", ReceiveType.USER);
        messageService.createMessage(discordItUser3.getId(), discordItUser4.getId(), "나도 정리 중이야. 내일 자료 같이 확인하자.", ReceiveType.USER);
        messageService.createMessage(discordItUser4.getId(), discordItUser3.getId(), "좋아! 저녁에 영상 통화하자.", ReceiveType.USER);

        // (3) discordItUser5 ↔ discordItUser6
        messageService.createMessage(discordItUser5.getId(), discordItUser6.getId(), "어제 경기 봤어?", ReceiveType.USER);
        messageService.createMessage(discordItUser6.getId(), discordItUser5.getId(), "봤지! 진짜 재밌더라.", ReceiveType.USER);
        messageService.createMessage(discordItUser5.getId(), discordItUser6.getId(), "그러니까. 마지막 골은 전율이었어.", ReceiveType.USER);
        messageService.createMessage(discordItUser6.getId(), discordItUser5.getId(), "담주에 같이 보러 갈래?", ReceiveType.USER);

        // (4) discordItUser7 ↔ discordItUser8
        messageService.createMessage(discordItUser7.getId(), discordItUser8.getId(), "새로 나온 게임 해봤어?", ReceiveType.USER);
        messageService.createMessage(discordItUser8.getId(), discordItUser7.getId(), "응! 그래픽이 진짜 대박이야.", ReceiveType.USER);
        messageService.createMessage(discordItUser7.getId(), discordItUser8.getId(), "같이 파티 맺고 해볼래?", ReceiveType.USER);
        messageService.createMessage(discordItUser8.getId(), discordItUser7.getId(), "좋지! 오늘 밤에 접속하자.", ReceiveType.USER);

        // 채널 생성
        Channel channel1 = channelService.createChannel(ChannelType.VOICE, "스터디 음성 채널", discordItUser1.getId());
        Channel channel2 = channelService.createChannel(ChannelType.MESSAGE, "프로젝트 토론 메시지 채널", discordItUser2.getId());
        Channel channel3 = channelService.createChannel(ChannelType.VOICE, "게임 파티 음성 채널", discordItUser3.getId());
        Channel channel4 = channelService.createChannel(ChannelType.MESSAGE, "공지사항 메시지 채널", discordItUser4.getId());
        Channel channel5 = channelService.createChannel(ChannelType.VOICE, "음악 감상 음성 채널", discordItUser5.getId());
        Channel channel6 = channelService.createChannel(ChannelType.MESSAGE, "자유 대화 메시지 채널", discordItUser6.getId());
        Channel channel7 = channelService.createChannel(ChannelType.VOICE, "코딩 면접 준비 음성 채널", discordItUser7.getId());
        Channel channel8 = channelService.createChannel(ChannelType.MESSAGE, "Q&A 메시지 채널", discordItUser8.getId());

        // 채널에 멤버 추가
        // channel1 (관리자: discordItUser1)
        channelService.addMember(channel1.getId(), discordItUser3.getId());
        channelService.addMember(channel1.getId(), discordItUser6.getId());

        // channel2 (관리자: discordItUser2)
        channelService.addMember(channel2.getId(), discordItUser1.getId());
        channelService.addMember(channel2.getId(), discordItUser4.getId());
        channelService.addMember(channel2.getId(), discordItUser6.getId());
        channelService.addMember(channel2.getId(), discordItUser8.getId());

        // channel3 (관리자: discordItUser3)
        channelService.addMember(channel3.getId(), discordItUser2.getId());
        channelService.addMember(channel3.getId(), discordItUser5.getId());
        channelService.addMember(channel3.getId(), discordItUser7.getId());

        // channel4 (관리자: discordItUser4)
        channelService.addMember(channel4.getId(), discordItUser1.getId());
        channelService.addMember(channel4.getId(), discordItUser2.getId());
        channelService.addMember(channel4.getId(), discordItUser6.getId());
        channelService.addMember(channel4.getId(), discordItUser7.getId());
        channelService.addMember(channel4.getId(), discordItUser8.getId());

        // channel5 (관리자: discordItUser5)
        channelService.addMember(channel5.getId(), discordItUser4.getId());

        // channel6 (관리자: discordItUser6)
        channelService.addMember(channel6.getId(), discordItUser2.getId());
        channelService.addMember(channel6.getId(), discordItUser3.getId());
        channelService.addMember(channel6.getId(), discordItUser5.getId());
        channelService.addMember(channel6.getId(), discordItUser7.getId());
        channelService.addMember(channel6.getId(), discordItUser8.getId());

        // channel7 (관리자: discordItUser7)
        channelService.addMember(channel7.getId(), discordItUser3.getId());
        channelService.addMember(channel7.getId(), discordItUser6.getId());

        // channel8 (관리자: discordItUser8)
        channelService.addMember(channel8.getId(), discordItUser1.getId());
        channelService.addMember(channel8.getId(), discordItUser2.getId());
        channelService.addMember(channel8.getId(), discordItUser3.getId());
        channelService.addMember(channel8.getId(), discordItUser4.getId());
        channelService.addMember(channel8.getId(), discordItUser5.getId());
        channelService.addMember(channel8.getId(), discordItUser6.getId());

        // 채널 메시지 추가(메시지 채널에만 추가)

        // channel2 (discordItUser2 관리자) : "프로젝트 토론 메시지 채널"
        messageService.createMessage(discordItUser2.getId(), channel2.getId(), "이번 주까지 로그인 기능 마무리해야 할 것 같아요.", ReceiveType.CHANNEL);
        messageService.createMessage(discordItUser4.getId(), channel2.getId(), "DB 쪽은 제가 맡을게요.", ReceiveType.CHANNEL);
        messageService.createMessage(discordItUser6.getId(), channel2.getId(), "UI는 제가 진행할게요.", ReceiveType.CHANNEL);
        messageService.createMessage(discordItUser8.getId(), channel2.getId(), "테스트 케이스 작성은 제가 해보겠습니다.", ReceiveType.CHANNEL);
        messageService.createMessage(discordItUser2.getId(), channel2.getId(), "좋아요. 그럼 목요일까지 중간 점검합시다.", ReceiveType.CHANNEL);
        messageService.createMessage(discordItUser1.getId(), channel2.getId(), "확인했습니다!", ReceiveType.CHANNEL);

        // channel4 (discordItUser4 관리자) : "공지사항 메시지 채널"
        messageService.createMessage(discordItUser4.getId(), channel4.getId(), "📢 이번 주 토요일 정기 점검이 있습니다.", ReceiveType.CHANNEL);
        messageService.createMessage(discordItUser1.getId(), channel4.getId(), "확인했습니다!", ReceiveType.CHANNEL);
        messageService.createMessage(discordItUser2.getId(), channel4.getId(), "네 알겠습니다.", ReceiveType.CHANNEL);
        messageService.createMessage(discordItUser6.getId(), channel4.getId(), "그날은 접속 못하겠네요 ㅎㅎ", ReceiveType.CHANNEL);
        messageService.createMessage(discordItUser4.getId(), channel4.getId(), "불편 드려 죄송합니다. 더 나은 환경을 위해 점검하는 거예요.", ReceiveType.CHANNEL);
        messageService.createMessage(discordItUser7.getId(), channel4.getId(), "넵 고생 많으십니다!", ReceiveType.CHANNEL);

        // channel6 (discordItUser6 관리자) : "자유 대화 메시지 채널"
        messageService.createMessage(discordItUser6.getId(), channel6.getId(), "오늘 점심 뭐 먹었어요?", ReceiveType.CHANNEL);
        messageService.createMessage(discordItUser2.getId(), channel6.getId(), "저는 김치찌개 먹었어요 ㅎㅎ", ReceiveType.CHANNEL);
        messageService.createMessage(discordItUser8.getId(), channel6.getId(), "저는 편의점 샌드위치로 때웠어요.", ReceiveType.CHANNEL);
        messageService.createMessage(discordItUser7.getId(), channel6.getId(), "저는 돈가스 먹었습니다!", ReceiveType.CHANNEL);
        messageService.createMessage(discordItUser3.getId(), channel6.getId(), "다들 맛있게 드셨네요. 전 샐러드만 먹었어요.", ReceiveType.CHANNEL);
        messageService.createMessage(discordItUser6.getId(), channel6.getId(), "역시 점심 메뉴 얘기가 제일 재밌어요 ㅋㅋ", ReceiveType.CHANNEL);

        // channel8 (discordItUser8 관리자) : "Q&A 메시지 채널"
        messageService.createMessage(discordItUser8.getId(), channel8.getId(), "스프링에서 DI가 뭔지 설명해줄 수 있나요?", ReceiveType.CHANNEL);
        messageService.createMessage(discordItUser5.getId(), channel8.getId(), "의존성 주입이에요. 객체 간 결합도를 낮추는 개념이죠.", ReceiveType.CHANNEL);
        messageService.createMessage(discordItUser3.getId(), channel8.getId(), "맞아요. 보통 @Autowired 같은 걸로 구현합니다.", ReceiveType.CHANNEL);
        messageService.createMessage(discordItUser2.getId(), channel8.getId(), "또는 생성자 주입 방식도 많이 씁니다.", ReceiveType.CHANNEL);
        messageService.createMessage(discordItUser6.getId(), channel8.getId(), "그게 테스트할 때 더 유리하다고 들었어요.", ReceiveType.CHANNEL);
        messageService.createMessage(discordItUser8.getId(), channel8.getId(), "아~ 이제 확실히 이해됐습니다. 감사합니다!", ReceiveType.CHANNEL);
    }
}
