package main.com.pos.view.dashboard;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class DashboardFrame extends JFrame {

    public DashboardFrame(String name) {
        setTitle("Dashboard");
        try {
            var image = javax.imageio.ImageIO.read(getClass().getClassLoader().getResourceAsStream("main/com/pos/resources/images/AppIcon.png"));
            setIconImage(image);
        } catch (IOException e) {
            System.err.println("❌ Failed to load app icon.");
        }
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1024, 640));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        GradientPanel root = new GradientPanel();
        root.setLayout(new BorderLayout());
        add(root, BorderLayout.CENTER);

        // Create sidebar
        JPanel sidebar = createSidebar();
        root.add(sidebar, BorderLayout.WEST);

        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setOpaque(false);
        root.add(mainContent, BorderLayout.CENTER);

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(24, 32, 12, 32));

        JLabel labelWelcome = new JLabel("Welcome, " + name, SwingConstants.LEFT);
        labelWelcome.setFont(new Font("JetBrains Mono", Font.PLAIN, 24));
        labelWelcome.setForeground(new Color(26, 46, 75));
        header.add(labelWelcome, BorderLayout.WEST);

        JLabel labelSub = new JLabel("Choose a module to get started", SwingConstants.RIGHT);
        labelSub.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
        labelSub.setForeground(new Color(80, 96, 112));
        header.add(labelSub, BorderLayout.EAST);

        mainContent.add(header, BorderLayout.NORTH);

        JPanel body = new JPanel(new GridBagLayout());
        body.setOpaque(false);
        body.setBorder(BorderFactory.createEmptyBorder(10, 32, 32, 32));
        mainContent.add(body, BorderLayout.CENTER);

        JPanel card = new RoundedPanel(18, new Color(255, 255, 255, 236), new Color(223, 230, 239));
        card.setLayout(new GridBagLayout());
        card.setBorder(BorderFactory.createEmptyBorder(28, 32, 28, 32));
        card.setPreferredSize(new Dimension(960, 520));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Control Center", SwingConstants.LEFT);
        title.setFont(new Font("JetBrains Mono SemiBold", Font.PLAIN, 22));
        title.setForeground(new Color(26, 46, 75));
        gbc.gridy = 0;
        card.add(title, gbc);

        JLabel subtitle = new JLabel("Manage products, run sales, and review performance", SwingConstants.LEFT);
        subtitle.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
        subtitle.setForeground(new Color(93, 109, 126));
        gbc.gridy = 1;
        card.add(subtitle, gbc);

        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;
        JPanel grid = new JPanel(new GridBagLayout());
        grid.setOpaque(false);
        card.add(grid, gbc);

        addModuleButton(grid, "Products", "Maintain catalog and stock.", 0, 0, new Color(59, 130, 246));
        addModuleButton(grid, "POS", "Create and settle new orders.", 1, 0, new Color(16, 185, 129));
        addModuleButton(grid, "Reports", "View revenue and trends.", 0, 1, new Color(236, 72, 153));
        addModuleButton(grid, "Settings", "Configure store preferences.", 1, 1, new Color(99, 102, 241));

        GridBagConstraints bodyConstraints = new GridBagConstraints();
        bodyConstraints.gridx = 0;
        bodyConstraints.gridy = 0;
        bodyConstraints.weightx = 1;
        bodyConstraints.weighty = 1;
        bodyConstraints.fill = GridBagConstraints.BOTH;
        body.add(card, bodyConstraints);
    }

    private void addModuleButton(JPanel parent, String title, String caption, int x, int y, Color baseColor) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.insets = new Insets(12, 12, 12, 12);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        constraints.weighty = 1;

        JPanel tile = new RoundedPanel(14, Color.WHITE, new Color(225, 232, 240));
        tile.setLayout(new BorderLayout());
        tile.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        JLabel labelTitle = new JLabel(title);
        labelTitle.setFont(new Font("JetBrains Mono SemiBold", Font.PLAIN, 18));
        labelTitle.setForeground(new Color(30, 41, 59));
        tile.add(labelTitle, BorderLayout.NORTH);

        JLabel labelCaption = new JLabel(caption);
        labelCaption.setFont(new Font("JetBrains Mono", Font.PLAIN, 13));
        labelCaption.setForeground(new Color(99, 115, 129));
        tile.add(labelCaption, BorderLayout.CENTER);

        JButton action = new RoundedButton("Open", 12);
        action.setFont(new Font("JetBrains Mono", Font.BOLD, 13));
        action.setBackground(baseColor);
        action.setForeground(Color.WHITE);
        action.setFocusPainted(false);
        action.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        tile.add(action, BorderLayout.SOUTH);

        parent.add(tile, constraints);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(new Color(15, 32, 58));
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JPanel logoPanel = new JPanel();
        logoPanel.setOpaque(false);
        logoPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        JLabel logo = new JLabel("POS");
        logo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 24));
        logo.setForeground(new Color(59, 130, 246));
        logoPanel.add(logo);
        sidebar.add(logoPanel, BorderLayout.NORTH);

        JPanel menuPanel = new JPanel();
        menuPanel.setOpaque(false);
        menuPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.gridy = 0;

        String[] menuItems = {"📊 Dashboard", "📦 Products", "🛒 POS Sales", "📈 Reports", "⚙️ Settings"};
        for (String item : menuItems) {
            JButton menuBtn = new JButton(item);
            menuBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
            menuBtn.setForeground(new Color(203, 213, 225));
            menuBtn.setBackground(new Color(15, 32, 58));
            menuBtn.setHorizontalAlignment(SwingConstants.LEFT);
            menuBtn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
            menuBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            menuBtn.setFocusPainted(false);
            menuBtn.setBorderPainted(false);
            menuBtn.setContentAreaFilled(false);

            menuBtn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    menuBtn.setForeground(new Color(59, 130, 246));
                    menuBtn.setOpaque(true);
                    menuBtn.setBackground(new Color(30, 58, 100));
                    menuBtn.setContentAreaFilled(true);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    menuBtn.setForeground(new Color(203, 213, 225));
                    menuBtn.setOpaque(false);
                    menuBtn.setContentAreaFilled(false);
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    menuBtn.setBackground(new Color(20, 45, 80));
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    menuBtn.setBackground(new Color(30, 58, 100));
                }
            });

            menuBtn.addActionListener(e -> {
                System.out.println("Clicked: " + item);
            });

            menuPanel.add(menuBtn, gbc);
            gbc.gridy++;
        }

        JPanel scrollWrapper = new JPanel(new BorderLayout());
        scrollWrapper.setOpaque(false);
        scrollWrapper.add(menuPanel, BorderLayout.NORTH);
        sidebar.add(scrollWrapper, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel();
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JButton logoutBtn = new JButton("🚪 Logout");
        logoutBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBackground(new Color(239, 68, 68));
        logoutBtn.setOpaque(true);
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        footerPanel.add(logoutBtn);
        sidebar.add(footerPanel, BorderLayout.SOUTH);

        return sidebar;
    }

    private static class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D graphics2d = (Graphics2D) g.create();
            graphics2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            int w = getWidth();
            int h = getHeight();
            GradientPaint gp = new GradientPaint(0, 0, new Color(240, 249, 255), w, h, new Color(226, 232, 240));
            graphics2d.setPaint(gp);
            graphics2d.fillRect(0, 0, w, h);
            graphics2d.dispose();
        }
    }

    private static class RoundedPanel extends JPanel {
        private final int radius;
        private final Color fill;
        private final Color stroke;

        RoundedPanel(int radius, Color fill, Color stroke) {
            this.radius = radius;
            this.fill = fill;
            this.stroke = stroke;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D graphics2D = (Graphics2D) graphics.create();
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.setColor(fill);
            graphics2D.fillRoundRect(0, 0, getWidth(), getHeight(), radius * 2, radius * 2);
            graphics2D.setColor(stroke);
            graphics2D.setStroke(new BasicStroke(1f));
            graphics2D.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius * 2, radius * 2);
            graphics2D.dispose();
            super.paintComponent(graphics);
        }
    }

    private static class RoundedButton extends JButton {
        private final int radius;

        RoundedButton(String text, int radius) {
            super(text);
            this.radius = radius;
            setContentAreaFilled(false);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D graphics2D = (Graphics2D) graphics.create();
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.setColor(getBackground());
            graphics2D.fillRoundRect(0, 0, getWidth(), getHeight(), radius * 2, radius * 2);
            super.paintComponent(graphics);
            graphics2D.dispose();
        }
    }
}
