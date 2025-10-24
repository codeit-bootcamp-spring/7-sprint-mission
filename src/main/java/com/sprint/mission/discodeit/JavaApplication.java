//package com.sprint.mission.discodeit;
//
//import com.sprint.mission.discodeit.utils.AppConfigRegacy;
//import com.sprint.mission.discodeit.view.*;
//
//import java.util.InputMismatchException;
//import java.util.Scanner;
//
//public class JavaApplication {
//
//    public static void main(String[] args) {
//        AppConfigRegacy appConfigRegacy = new AppConfigRegacy();
//        Scanner sc = new Scanner(System.in);
////        Runtime.getRuntime().addShutdownHook(new Thread(appConfigRegacy::saveAllData));
//
//        // 1. 공통 헬퍼 View를 먼저 생성합니다.
//        SharedView sharedView = new SharedView(appConfigRegacy, sc);
//
//        // 2. 각 도메인 View를 생성하며, 필요에 따라 SharedView를 주입합니다.
//        UserView userView = new UserView(appConfigRegacy, sc, sharedView);
//        ChannelView channelView = new ChannelView(appConfigRegacy, sc, sharedView);
//        ParticipationView participationView = new ParticipationView(appConfigRegacy, sc, sharedView);
//        MessageView messageView = new MessageView(appConfigRegacy, sc, sharedView);
//
//        while (true) {
//            try {
//                System.out.println("\n==================================================");
//                System.out.println(" DiscodeIt 콘솔 테스트 V1.0 ");
//                System.out.println("==================================================");
//                System.out.println("1. 유저 관리");
//                System.out.println("2. 채널 관리");
//                System.out.println("3. 참여 관리");
//                System.out.println("4. 메시지 관리");
//                System.out.println("0. 종료 (데이터 저장)");
//                System.out.print(">> 메뉴를 선택하세요: ");
//
//                int choice = sc.nextInt();
//                sc.nextLine();
//
//                switch (choice) {
//                    case 1:
//                        userView.showUserMenu();
//                        break;
//                    case 2:
//                        channelView.showChannelMenu();
//                        break;
//                    case 3:
//                        participationView.showParticipationMenu();
//                        break;
//                    case 4:
//                        messageView.showMessageMenu();
//                        break;
//                    case 0:
//                        System.out.println("프로그램을 종료합니다. 데이터가 저장됩니다.");
//                        return;
//                    default:
//                        System.out.println("잘못된 입력입니다. 다시 시도해주세요.");
//                }
//            } catch (InputMismatchException e) {
//                System.out.println("숫자를 입력해주세요.");
//                sc.nextLine();
//            } catch (Exception e) {
//                System.out.println("오류가 발생했습니다: " + e.getMessage());
//            }
//        }
//    }
//}