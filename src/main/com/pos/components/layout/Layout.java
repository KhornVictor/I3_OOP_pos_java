package main.com.pos.components.layout;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import main.com.pos.components.ui.UI;
import main.com.pos.model.User;
import main.com.pos.view.dashboard.DashboardPanel;

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
        ContentPanel contentPanel = new ContentPanel(user);
        Navigation navigation = new Navigation(user, "Dashboard", contentPanel, null);
        SideBar sideBar = new SideBar(user, navigation, contentPanel);
        navigation = new Navigation(user, "Dashboard", contentPanel, sideBar);
        sideBar = new SideBar(user, navigation, contentPanel);
        
        // Add initial dashboard panel
        contentPanel.removeAll();
        contentPanel.add(new DashboardPanel(contentPanel, user, navigation, sideBar), BorderLayout.CENTER);
        
        mainContent.add(navigation, BorderLayout.NORTH);
        mainContent.add(contentPanel, BorderLayout.CENTER);
        
        add(root, BorderLayout.CENTER);
        root.add(sideBar, BorderLayout.WEST);
        root.add(mainContent, BorderLayout.CENTER);
    }
}