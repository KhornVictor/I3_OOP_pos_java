package main.com.pos.thread;

import java.awt.EventQueue;
import main.com.pos.util.Color;
import main.com.pos.view.login.LoginFrame;

public class LoginTask implements Runnable {

    @Override
    public void run() {
        // User user = new User(
        //     "a20260001" ,"victor", "123456", "admin", "Khorn Victor", "victor@gmail.com", 1, "images/avatar/victor.png"
        // );
        long startTime = System.currentTimeMillis();
        EventQueue.invokeLater(() -> {new LoginFrame().setVisible(true); });
        long endTime = System.currentTimeMillis();
        System.out.println(Color.GREEN + "🛬  Login task completed in " + (endTime - startTime) + " ms" + Color.RESET);
    }
}
