package main.com.pos.components.layout;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import main.com.pos.components.ui.UI;
import main.com.pos.model.User;

public class Layout extends JFrame {
    public Layout(User user) {
        setTitle("Point of Sale System");
        setIconImage(UI.setApplicationIcon("main/com/pos/resources/images/AppIcon.png"));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1024, 640));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        JPanel root = new JPanel(new BorderLayout());
        root.setOpaque(false);

        JPanel mainContent = new MainPanel();
        Navigation navigation = new Navigation(user, "Welcome, " + user.getName());
        mainContent.add(navigation, BorderLayout.NORTH);
        mainContent.add(new ContentPanel(), BorderLayout.CENTER);
        
        add(root, BorderLayout.CENTER);
        root.add(new SideBar(user, navigation), BorderLayout.WEST);
        root.add(mainContent, BorderLayout.CENTER);
    }
}