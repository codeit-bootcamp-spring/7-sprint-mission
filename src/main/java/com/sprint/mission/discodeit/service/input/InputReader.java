package com.sprint.mission.discodeit.service.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InputReader {
    private BufferedReader br;


    public InputReader() {
        this.br= new BufferedReader(new InputStreamReader(System.in));
    }

    public String readLine() {
        try{
            return br.readLine().trim();
        }catch (IOException e){
            System.out.println("입력 오류: " + e.getMessage());
            return "";
        }
    }

    public void close() {
        try {
            if (br != null) {
                br.close();
            }
        } catch (IOException e) {
            System.err.println("종료 오류: " + e.getMessage());
        }
    }

}
