package main.com.pos.components.layout;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import main.com.pos.components.loading.LoadingPanel;
import main.com.pos.components.ui.UI;
import main.com.pos.model.User;

public class Layout extends JFrame {
    private final LoadingPanel loadingPanel;
    private ContentPanel contentPanel;

    public Layout(User user) {
        setTitle("Point of Sale System");
        setIconImage(UI.setApplicationIcon("main/com/pos/resources/images/AppIcon.png"));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1024, 640));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Show loading panel immediately
        loadingPanel = new LoadingPanel();
        loadingPanel.setProgress(5, "Initializing...");
        add(loadingPanel, BorderLayout.CENTER);
        setVisible(true);
        SwingUtilities.invokeLater(() -> {
            Thread initThread = new Thread(() -> initializeUI(user), "Layout-Init");
            initThread.setDaemon(false);
            initThread.start();
        });
    }

    private void initializeUI(User user) {
        try {
            updateProgress(15, "Loading configuration...", 100);
            
            JPanel root = new JPanel(new BorderLayout());
            root.setOpaque(false);
            
            updateProgress(35, "Setting up layout...", 100);
            
            JPanel mainContent = new MainPanel();
            contentPanel = new ContentPanel(user);
            
            updateProgress(50, "Creating panels...", 100);
            updateProgress(65, "Initializing navigation...", 100);
            
            Navigation navigation = new Navigation(user, "Dashboard", contentPanel, null);
            SideBar sideBar = new SideBar(user, navigation, contentPanel);
            navigation = new Navigation(user, "Dashboard", contentPanel, sideBar);
            sideBar = new SideBar(user, navigation, contentPanel);
            
            updateProgress(80, "Loading dashboard...", 100);
            
            mainContent.add(navigation, BorderLayout.NORTH);
            mainContent.add(contentPanel, BorderLayout.CENTER);
            
            updateProgress(95, "Finalizing...", 100);
            
            // Add components to frame
            root.add(sideBar, BorderLayout.WEST);
            root.add(mainContent, BorderLayout.CENTER);
            
            // Complete loading and replace with main content
            updateProgress(100, "Ready!", 150);
            
            SwingUtilities.invokeLater(() -> {
                remove(loadingPanel);
                add(root, BorderLayout.CENTER);
                revalidate();
                repaint();
            });
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void updateProgress(int progress, String status, long delayMs) throws InterruptedException {
        SwingUtilities.invokeLater(() -> loadingPanel.setProgress(progress, status));
        Thread.sleep(delayMs);
    }
}