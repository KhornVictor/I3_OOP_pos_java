package main.com.pos.thread;

import java.awt.EventQueue;
import main.com.pos.view.login.LoginFrame;

public class LoginTask implements Runnable {

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        EventQueue.invokeLater(() -> {new LoginFrame().setVisible(true); });
        long endTime = System.currentTimeMillis();
        System.out.println("Login task completed in " + (endTime - startTime) + " ms");
    }
}
