package main.com.pos.components.layout;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import main.com.pos.view.dashboard.DashboardPanel;

public class ContentPanel extends JPanel {

    DashboardPanel dashboardPanel = new DashboardPanel();

    public ContentPanel(){
        setLayout(new BorderLayout());
        setBackground(new Color(248, 250, 252));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(welcomePanel(), BorderLayout.CENTER);
        add(dashboardPanel, BorderLayout.CENTER);
    }

    static JPanel welcomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.removeAll();
        panel.revalidate();
        panel.repaint();
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        panel.setBackground(new Color(248, 250, 252));

        JPanel contentWrapper = new JPanel();
        contentWrapper.setLayout(new javax.swing.BoxLayout(contentWrapper, javax.swing.BoxLayout.Y_AXIS));
        contentWrapper.setBackground(new Color(248, 250, 252));
        contentWrapper.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel welcomeTitle = new JLabel("Welcome to POS System");
        welcomeTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 28));
        welcomeTitle.setForeground(new Color(15, 32, 58));
        welcomeTitle.setAlignmentX(javax.swing.JLabel.CENTER_ALIGNMENT);

        JLabel welcomeSubtitle = new JLabel("Point of Sale Management System");
        welcomeSubtitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        welcomeSubtitle.setForeground(new Color(100, 116, 139));
        welcomeSubtitle.setAlignmentX(javax.swing.JLabel.CENTER_ALIGNMENT);

        JLabel instructionLabel = new JLabel("Use the sidebar menu to navigate through the system");
        instructionLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
        instructionLabel.setForeground(new Color(120, 140, 160));
        instructionLabel.setAlignmentX(javax.swing.JLabel.CENTER_ALIGNMENT);

        contentWrapper.add(javax.swing.Box.createVerticalStrut(50));
        contentWrapper.add(welcomeTitle);
        contentWrapper.add(javax.swing.Box.createVerticalStrut(10));
        contentWrapper.add(welcomeSubtitle);
        contentWrapper.add(javax.swing.Box.createVerticalStrut(30));
        contentWrapper.add(instructionLabel);

        panel.add(contentWrapper, BorderLayout.CENTER);
        return panel;
    }
}
