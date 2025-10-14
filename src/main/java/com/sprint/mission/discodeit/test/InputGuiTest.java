package com.sprint.mission.discodeit.test;

import com.sprint.mission.discodeit.service.input.InputApplication;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class InputGuiTest {
    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("FrontGui");
        frame.setSize(600,300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel label1 = new JLabel("안녕하세요 비즈니스 로직 테스트 창입니다",SwingConstants.CENTER);
        JButton button1 = new JButton("출력창 초기화 버튼입니당");
        JTextField inputField = new JTextField(40);
        JTextArea outputArea = new JTextArea(10,40);
        JScrollPane scrollPane = new JScrollPane(outputArea);



        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        PipedInputStream pipedInputStream = new PipedInputStream(pipedOutputStream);
        System.setIn(pipedInputStream);
        ActionListener inputAction = e ->{
            String textInput = inputField.getText();
            if(!textInput.isEmpty()){
                try {
                    outputArea.append("입력 커맨드 : " +textInput + "\n");

                    pipedOutputStream.write((textInput + "\n").getBytes(StandardCharsets.UTF_8));
                    pipedOutputStream.flush();
                    inputField.setText("");

                }
                catch (IOException ex){
                    ex.printStackTrace();
                }

            }
        };
        ActionListener clearAction = e -> outputArea.setText("");

        button1.addActionListener(clearAction);
        outputArea.setEditable(false);

        PrintStream printStream = new PrintStream(new OutputStream()
        {
            @Override
            public void write(int b) throws java.io.IOException {
                outputArea.append(String.valueOf((char)b));
                outputArea.setCaretPosition(outputArea.getDocument().getLength());
            }
        }, true, StandardCharsets.UTF_8

        );



        System.setOut(printStream);
        System.setErr(printStream);
        inputField.addKeyListener(new KeyAdapter() {
        @Override
            public void keyPressed(KeyEvent e){
            if(e.getKeyCode()== KeyEvent.VK_ENTER){
                inputAction.actionPerformed(null);
            }
        }
        });
        JPanel panel = new JPanel();
        panel.add(label1);
        panel.add(button1);
        panel.add(inputField);

        panel.add(scrollPane);

        frame.add(panel);
        frame.setVisible(true);

        InputApplication inputApplication = new InputApplication();
        inputApplication.start();
    }
}
