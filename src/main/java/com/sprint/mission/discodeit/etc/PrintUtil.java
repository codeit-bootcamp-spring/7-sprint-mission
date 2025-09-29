package com.sprint.mission.discodeit.etc;

//밑줄 편하게 그으려고 만든 클래스
public class PrintUtil {
    public static void printLine(){
        System.out.println("=========================================");
    }

    //밑줄 긋는 메서드가 스태틱이니 클래스 객체를 만들 필요가 없음 -> private접근자로 막아버리기
    private PrintUtil() {
    }
}
