package main.com.pos;

import java.awt.EventQueue;
import main.com.pos.test.TestFrame;

public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            new TestFrame().setVisible(true);
        });
    }
}