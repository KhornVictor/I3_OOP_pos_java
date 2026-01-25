package main.com.pos.view.user;

import javax.swing.JFrame;

public class UserFrame {
    public static void main(String[] args) {
        JFrame frame = new JFrame("User Frame");
        frame.setSize(400, 300);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.add(new UserPanel());
        
    }
}