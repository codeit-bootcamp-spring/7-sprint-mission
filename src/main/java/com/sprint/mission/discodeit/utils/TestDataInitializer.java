package com.sprint.mission.discodeit.utils;

import com.sprint.mission.discodeit.dto.channel.request.CreateChannelRequestDto;
import com.sprint.mission.discodeit.dto.channel.request.UpdateChannelRequestDto;
import com.sprint.mission.discodeit.dto.channel.response.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.response.PrivateChannelResponseDto;
import com.sprint.mission.discodeit.dto.message.request.CreateMessageRequestDto;
import com.sprint.mission.discodeit.dto.user.request.CreateUserRequestDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
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
        messageService.createMessage(new CreateMessageRequestDto(discordItUser1.getId(), discordItUser2.getId(), "안녕하세요, 잘 지내시죠?", ReceiveType.USER, null));
        messageService.createMessage(new CreateMessageRequestDto(discordItUser2.getId(), discordItUser1.getId(), "네! 오랜만이에요.", ReceiveType.USER, null));
        messageService.createMessage(new CreateMessageRequestDto(discordItUser1.getId(), discordItUser2.getId(), "이번 주말에 시간 괜찮으세요?", ReceiveType.USER, null));
        messageService.createMessage(new CreateMessageRequestDto(discordItUser2.getId(), discordItUser1.getId(), "네, 좋아요. 같이 영화 볼까요?", ReceiveType.USER, null));

        // (2) discordItUser3 ↔ discordItUser4
        messageService.createMessage(new CreateMessageRequestDto(discordItUser3.getId(), discordItUser4.getId(), "오늘 회의 준비는 잘 하고 있어?", ReceiveType.USER, null));
        messageService.createMessage(new CreateMessageRequestDto(discordItUser4.getId(), discordItUser3.getId(), "응, 거의 다 끝났어. 너는?", ReceiveType.USER, null));
        messageService.createMessage(new CreateMessageRequestDto(discordItUser3.getId(), discordItUser4.getId(), "나도 정리 중이야. 내일 자료 같이 확인하자.", ReceiveType.USER, null));
        messageService.createMessage(new CreateMessageRequestDto(discordItUser4.getId(), discordItUser3.getId(), "좋아! 저녁에 영상 통화하자.", ReceiveType.USER, null));

        // (3) discordItUser5 ↔ discordItUser6
        messageService.createMessage(new CreateMessageRequestDto(discordItUser5.getId(), discordItUser6.getId(), "어제 경기 봤어?", ReceiveType.USER, null));
        messageService.createMessage(new CreateMessageRequestDto(discordItUser6.getId(), discordItUser5.getId(), "봤지! 진짜 재밌더라.", ReceiveType.USER, null));
        messageService.createMessage(new CreateMessageRequestDto(discordItUser5.getId(), discordItUser6.getId(), "그러니까. 마지막 골은 전율이었어.", ReceiveType.USER, null));
        messageService.createMessage(new CreateMessageRequestDto(discordItUser6.getId(), discordItUser5.getId(), "담주에 같이 보러 갈래?", ReceiveType.USER, null));

        // (4) discordItUser7 ↔ discordItUser8
        messageService.createMessage(new CreateMessageRequestDto(discordItUser7.getId(), discordItUser8.getId(), "새로 나온 게임 해봤어?", ReceiveType.USER, null));
        messageService.createMessage(new CreateMessageRequestDto(discordItUser8.getId(), discordItUser7.getId(), "응! 그래픽이 진짜 대박이야.", ReceiveType.USER, null));
        messageService.createMessage(new CreateMessageRequestDto(discordItUser7.getId(), discordItUser8.getId(), "같이 파티 맺고 해볼래?", ReceiveType.USER, null));
        messageService.createMessage(new CreateMessageRequestDto(discordItUser8.getId(), discordItUser7.getId(), "좋지! 오늘 밤에 접속하자.", ReceiveType.USER, null));

        // 채널 생성
        PrivateChannelResponseDto channel1 = channelService.createPrivateChannel(new CreateChannelRequestDto(ChannelType.VOICE, "스터디 음성 채널", discordItUser1.getId()));
        PrivateChannelResponseDto channel2 = channelService.createPrivateChannel(new CreateChannelRequestDto(ChannelType.MESSAGE, "프로젝트 토론 메시지 채널", discordItUser2.getId()));
        PrivateChannelResponseDto channel3 = channelService.createPrivateChannel(new CreateChannelRequestDto(ChannelType.VOICE, "게임 파티 음성 채널", discordItUser3.getId()));
        PrivateChannelResponseDto channel4 = channelService.createPrivateChannel(new CreateChannelRequestDto(ChannelType.MESSAGE, "공지사항 메시지 채널", discordItUser4.getId()));
        PrivateChannelResponseDto channel5 = channelService.createPrivateChannel(new CreateChannelRequestDto(ChannelType.VOICE, "음악 감상 음성 채널", discordItUser5.getId()));
        PrivateChannelResponseDto channel6 = channelService.createPrivateChannel(new CreateChannelRequestDto(ChannelType.MESSAGE, "자유 대화 메시지 채널", discordItUser6.getId()));
        PrivateChannelResponseDto channel7 = channelService.createPrivateChannel(new CreateChannelRequestDto(ChannelType.VOICE, "코딩 면접 준비 음성 채널", discordItUser7.getId()));
        PrivateChannelResponseDto channel8 = channelService.createPrivateChannel(new CreateChannelRequestDto(ChannelType.MESSAGE, "Q&A 메시지 채널", discordItUser8.getId()));

        // 채널에 멤버 추가
        // channel1 (관리자: discordItUser1)
        channelService.addMember(new UpdateChannelRequestDto(channel1.getId(), discordItUser3.getId()));
        channelService.addMember(new UpdateChannelRequestDto(channel1.getId(), discordItUser6.getId()));

        // channel2 (관리자: discordItUser2)
        channelService.addMember(new UpdateChannelRequestDto(channel2.getId(), discordItUser1.getId()));
        channelService.addMember(new UpdateChannelRequestDto(channel2.getId(), discordItUser4.getId()));
        channelService.addMember(new UpdateChannelRequestDto(channel2.getId(), discordItUser6.getId()));
        channelService.addMember(new UpdateChannelRequestDto(channel2.getId(), discordItUser8.getId()));

        // channel3 (관리자: discordItUser3)
        channelService.addMember(new UpdateChannelRequestDto(channel3.getId(), discordItUser2.getId()));
        channelService.addMember(new UpdateChannelRequestDto(channel3.getId(), discordItUser5.getId()));
        channelService.addMember(new UpdateChannelRequestDto(channel3.getId(), discordItUser7.getId()));

        // channel4 (관리자: discordItUser4)
        channelService.addMember(new UpdateChannelRequestDto(channel4.getId(), discordItUser1.getId()));
        channelService.addMember(new UpdateChannelRequestDto(channel4.getId(), discordItUser2.getId()));
        channelService.addMember(new UpdateChannelRequestDto(channel4.getId(), discordItUser6.getId()));
        channelService.addMember(new UpdateChannelRequestDto(channel4.getId(), discordItUser7.getId()));
        channelService.addMember(new UpdateChannelRequestDto(channel4.getId(), discordItUser8.getId()));

        // channel5 (관리자: discordItUser5)
        channelService.addMember(new UpdateChannelRequestDto(channel5.getId(), discordItUser4.getId()));

        // channel6 (관리자: discordItUser6)
        channelService.addMember(new UpdateChannelRequestDto(channel6.getId(), discordItUser2.getId()));
        channelService.addMember(new UpdateChannelRequestDto(channel6.getId(), discordItUser3.getId()));
        channelService.addMember(new UpdateChannelRequestDto(channel6.getId(), discordItUser5.getId()));
        channelService.addMember(new UpdateChannelRequestDto(channel6.getId(), discordItUser7.getId()));
        channelService.addMember(new UpdateChannelRequestDto(channel6.getId(), discordItUser8.getId()));

        // channel7 (관리자: discordItUser7)
        channelService.addMember(new UpdateChannelRequestDto(channel7.getId(), discordItUser3.getId()));
        channelService.addMember(new UpdateChannelRequestDto(channel7.getId(), discordItUser6.getId()));

        // channel8 (관리자: discordItUser8)
        channelService.addMember(new UpdateChannelRequestDto(channel8.getId(), discordItUser1.getId()));
        channelService.addMember(new UpdateChannelRequestDto(channel8.getId(), discordItUser2.getId()));
        channelService.addMember(new UpdateChannelRequestDto(channel8.getId(), discordItUser3.getId()));
        channelService.addMember(new UpdateChannelRequestDto(channel8.getId(), discordItUser4.getId()));
        channelService.addMember(new UpdateChannelRequestDto(channel8.getId(), discordItUser5.getId()));
        channelService.addMember(new UpdateChannelRequestDto(channel8.getId(), discordItUser6.getId()));

        // 채널 메시지 추가(메시지 채널에만 추가)

        // channel2 (discordItUser2 관리자) : "프로젝트 토론 메시지 채널"
        messageService.createMessage(new CreateMessageRequestDto(discordItUser2.getId(), channel2.getId(), "이번 주까지 로그인 기능 마무리해야 할 것 같아요.", ReceiveType.CHANNEL, null));
        messageService.createMessage(new CreateMessageRequestDto(discordItUser4.getId(), channel2.getId(), "DB 쪽은 제가 맡을게요.", ReceiveType.CHANNEL, null));
        messageService.createMessage(new CreateMessageRequestDto(discordItUser6.getId(), channel2.getId(), "UI는 제가 진행할게요.", ReceiveType.CHANNEL, null));
        messageService.createMessage(new CreateMessageRequestDto(discordItUser8.getId(), channel2.getId(), "테스트 케이스 작성은 제가 해보겠습니다.", ReceiveType.CHANNEL, null));
        messageService.createMessage(new CreateMessageRequestDto(discordItUser2.getId(), channel2.getId(), "좋아요. 그럼 목요일까지 중간 점검합시다.", ReceiveType.CHANNEL, null));
        messageService.createMessage(new CreateMessageRequestDto(discordItUser1.getId(), channel2.getId(), "확인했습니다!", ReceiveType.CHANNEL, null));

        // channel4 (discordItUser4 관리자) : "공지사항 메시지 채널"
        messageService.createMessage(new CreateMessageRequestDto(discordItUser4.getId(), channel4.getId(), "📢 이번 주 토요일 정기 점검이 있습니다.", ReceiveType.CHANNEL, null));
        messageService.createMessage(new CreateMessageRequestDto(discordItUser1.getId(), channel4.getId(), "확인했습니다!", ReceiveType.CHANNEL, null));
        messageService.createMessage(new CreateMessageRequestDto(discordItUser2.getId(), channel4.getId(), "네 알겠습니다.", ReceiveType.CHANNEL, null));
        messageService.createMessage(new CreateMessageRequestDto(discordItUser6.getId(), channel4.getId(), "그날은 접속 못하겠네요 ㅎㅎ", ReceiveType.CHANNEL, null));
        messageService.createMessage(new CreateMessageRequestDto(discordItUser4.getId(), channel4.getId(), "불편 드려 죄송합니다. 더 나은 환경을 위해 점검하는 거예요.", ReceiveType.CHANNEL, null));
        messageService.createMessage(new CreateMessageRequestDto(discordItUser7.getId(), channel4.getId(), "넵 고생 많으십니다!", ReceiveType.CHANNEL, null));

        // channel6 (discordItUser6 관리자) : "자유 대화 메시지 채널"
        messageService.createMessage(new CreateMessageRequestDto(discordItUser6.getId(), channel6.getId(), "오늘 점심 뭐 먹었어요?", ReceiveType.CHANNEL, null));
        messageService.createMessage(new CreateMessageRequestDto(discordItUser2.getId(), channel6.getId(), "저는 김치찌개 먹었어요 ㅎㅎ", ReceiveType.CHANNEL, null));
        messageService.createMessage(new CreateMessageRequestDto(discordItUser8.getId(), channel6.getId(), "저는 편의점 샌드위치로 때웠어요.", ReceiveType.CHANNEL, null));
        messageService.createMessage(new CreateMessageRequestDto(discordItUser7.getId(), channel6.getId(), "저는 돈가스 먹었습니다!", ReceiveType.CHANNEL, null));
        messageService.createMessage(new CreateMessageRequestDto(discordItUser3.getId(), channel6.getId(), "다들 맛있게 드셨네요. 전 샐러드만 먹었어요.", ReceiveType.CHANNEL, null));
        messageService.createMessage(new CreateMessageRequestDto(discordItUser6.getId(), channel6.getId(), "역시 점심 메뉴 얘기가 제일 재밌어요 ㅋㅋ", ReceiveType.CHANNEL, null));

        // channel8 (discordItUser8 관리자) : "Q&A 메시지 채널"
        messageService.createMessage(new CreateMessageRequestDto(discordItUser8.getId(), channel8.getId(), "스프링에서 DI가 뭔지 설명해줄 수 있나요?", ReceiveType.CHANNEL, null));
        messageService.createMessage(new CreateMessageRequestDto(discordItUser5.getId(), channel8.getId(), "의존성 주입이에요. 객체 간 결합도를 낮추는 개념이죠.", ReceiveType.CHANNEL, null));
        messageService.createMessage(new CreateMessageRequestDto(discordItUser3.getId(), channel8.getId(), "맞아요. 보통 @Autowired 같은 걸로 구현합니다.", ReceiveType.CHANNEL, null));
        messageService.createMessage(new CreateMessageRequestDto(discordItUser2.getId(), channel8.getId(), "또는 생성자 주입 방식도 많이 씁니다.", ReceiveType.CHANNEL, null));
        messageService.createMessage(new CreateMessageRequestDto(discordItUser6.getId(), channel8.getId(), "그게 테스트할 때 더 유리하다고 들었어요.", ReceiveType.CHANNEL, null));
        messageService.createMessage(new CreateMessageRequestDto(discordItUser8.getId(), channel8.getId(), "아~ 이제 확실히 이해됐습니다. 감사합니다!", ReceiveType.CHANNEL, null));


        // 공개 채널 추가
        ChannelResponseDto channel9 = channelService.createPublicChannel(new CreateChannelRequestDto(ChannelType.MESSAGE, "잡담 라운지", discordItUser1.getId()));
        ChannelResponseDto channel10 = channelService.createPublicChannel(new CreateChannelRequestDto(ChannelType.VOICE, "실시간 수다방", discordItUser2.getId()));
        ChannelResponseDto channel11 = channelService.createPublicChannel(new CreateChannelRequestDto(ChannelType.MESSAGE, "지식 나눔 메시지 채널", discordItUser3.getId()));
        ChannelResponseDto channel12 = channelService.createPublicChannel(new CreateChannelRequestDto(ChannelType.VOICE, "토론 세션 음성 채널", discordItUser4.getId()));

        // 채널 9: 잡담 라운지
        messageService.createMessage(new CreateMessageRequestDto(discordItUser1.getId(), channel9.getId(), "오늘 점심 뭐 먹을까요?", ReceiveType.CHANNEL, null));
        messageService.createMessage(new CreateMessageRequestDto(discordItUser2.getId(), channel9.getId(), "저는 김치찌개요!", ReceiveType.CHANNEL, null));
        messageService.createMessage(new CreateMessageRequestDto(discordItUser3.getId(), channel9.getId(), "점심 대신 커피 마시고 싶네요 ☕", ReceiveType.CHANNEL, null));
        messageService.createMessage(new CreateMessageRequestDto(discordItUser4.getId(), channel9.getId(), "오늘 날씨 진짜 좋네요.", ReceiveType.CHANNEL, null));

        // 채널 10: 실시간 수다방 (VOICE 타입)
        messageService.createMessage(new CreateMessageRequestDto(discordItUser5.getId(), channel10.getId(), "지금 다들 접속 중인가요?", ReceiveType.CHANNEL, null));
        messageService.createMessage(new CreateMessageRequestDto(discordItUser6.getId(), channel10.getId(), "마이크 테스트 중입니다~", ReceiveType.CHANNEL, null));
        messageService.createMessage(new CreateMessageRequestDto(discordItUser7.getId(), channel10.getId(), "소리 잘 들립니다!", ReceiveType.CHANNEL, null));

        // 채널 11: 지식 나눔 메시지 채널
        messageService.createMessage(new CreateMessageRequestDto(discordItUser2.getId(), channel11.getId(), "Spring Bean과 Component의 차이 아시나요?", ReceiveType.CHANNEL, null));
        messageService.createMessage(new CreateMessageRequestDto(discordItUser3.getId(), channel11.getId(), "Bean은 주로 설정 클래스에서, Component는 자동 스캔 대상이죠!", ReceiveType.CHANNEL, null));
        messageService.createMessage(new CreateMessageRequestDto(discordItUser1.getId(), channel11.getId(), "맞아요. 역할은 비슷하지만 등록 방식이 달라요.", ReceiveType.CHANNEL, null));
        messageService.createMessage(new CreateMessageRequestDto(discordItUser8.getId(), channel11.getId(), "좋은 정보 감사합니다!", ReceiveType.CHANNEL, null));

        // 채널 12: 토론 세션 음성 채널
        messageService.createMessage(new CreateMessageRequestDto(discordItUser4.getId(), channel12.getId(), "오늘 주제는 AI 윤리로 하죠.", ReceiveType.CHANNEL, null));
        messageService.createMessage(new CreateMessageRequestDto(discordItUser5.getId(), channel12.getId(), "좋아요. 요즘 AI 생성물 저작권 이슈가 크죠.", ReceiveType.CHANNEL, null));
        messageService.createMessage(new CreateMessageRequestDto(discordItUser6.getId(), channel12.getId(), "AI 책임 범위가 아직 명확하지 않아요.", ReceiveType.CHANNEL, null));
        messageService.createMessage(new CreateMessageRequestDto(discordItUser7.getId(), channel12.getId(), "법적 기준이 빨리 정립돼야 할 듯합니다.", ReceiveType.CHANNEL, null));
        messageService.createMessage(new CreateMessageRequestDto(discordItUser8.getId(), channel12.getId(), "토론 너무 재밌네요!", ReceiveType.CHANNEL, null));
    }
}
