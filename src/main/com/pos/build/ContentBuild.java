package main.com.pos.build;

import javax.swing.JFrame;
import javax.swing.JPanel;
import main.com.pos.components.layout.Layout;
import main.com.pos.model.User;
import main.com.pos.view.dashboard.DashboardPanel;

public class ContentBuild {

    public static JFrame frame;
    public static JPanel dashboardPanel;

    public static void build(User user) {
        frame = new Layout(user);
        frame.setVisible(true);
    }

    public void buildDashboard() {
        dashboardPanel = new DashboardPanel();  
        dashboardPanel.setVisible(false);
    }

    public static void setdashboardVisible(boolean isVisible) {
        
    }
}
