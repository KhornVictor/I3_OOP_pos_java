package main.com.pos.components.layout;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import main.com.pos.model.User;
import main.com.pos.view.welcome.WelcomePanel;

public class ContentPanel extends JPanel {
    public ContentPanel(User user) {
        setLayout(new BorderLayout());
        setBackground(new Color(248, 250, 252));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(new WelcomePanel(user), BorderLayout.CENTER);
    }
}
