package main.com.pos.components.loading;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Clean modern white loading screen for POS System
 */
public class LoadingPanel extends JPanel {

    private JProgressBar progressBar;
    private JLabel statusLabel;
    private JLabel percentLabel;

    private int progress = 0;
    private int dotCount = 0;

    public LoadingPanel() {
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 247, 250)); // soft white background

        initCard();
        startStatusAnimation();
    }

    // ---------------- UI ----------------

    private void initCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                // Card background
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 28, 28);

                // Soft shadow effect
                g2.setColor(new Color(0, 0, 0, 15));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 28, 28);

                g2.dispose();
            }
        };

        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(45, 70, 45, 70));
        card.setPreferredSize(new Dimension(520, 330));

        // ---- Title
        JLabel title = new JLabel("I3 POS System");
        title.setFont(new Font("Segoe UI", Font.BOLD, 40));
        title.setForeground(new Color(30, 30, 30));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Smart Point of Sale Platform");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subtitle.setForeground(new Color(110, 110, 110));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ---- Progress
        progressBar = new JProgressBar(0, 100);
        progressBar.setPreferredSize(new Dimension(380, 8));
        progressBar.setMaximumSize(new Dimension(380, 8));
        progressBar.setBorderPainted(false);
        progressBar.setStringPainted(false);
        progressBar.setBackground(new Color(230, 230, 230));
        progressBar.setForeground(new Color(37, 99, 235)); // modern blue
        progressBar.setUI(new ModernProgressBarUI());

        percentLabel = new JLabel("0%");
        percentLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        percentLabel.setForeground(new Color(40, 40, 40));
        percentLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        statusLabel = new JLabel("Initializing");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusLabel.setForeground(new Color(120, 120, 120));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel version = new JLabel("v1.0.0  •  © 2026");
        version.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        version.setForeground(new Color(150, 150, 150));
        version.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(title);
        card.add(Box.createVerticalStrut(8));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(28));
        card.add(percentLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(progressBar);
        card.add(Box.createVerticalStrut(18));
        card.add(statusLabel);
        card.add(Box.createVerticalGlue());
        card.add(version);

        add(card);
    }

    // ---------------- Status "Loading..." Animation ----------------

    private void startStatusAnimation() {
        new Timer(500, e -> {
            dotCount = (dotCount + 1) % 4;
            String dots = ".".repeat(dotCount);
            statusLabel.setText("Loading" + dots);
        }).start();
    }

    // ---------------- Public API ----------------

    public void setProgress(int value, String status) {
        SwingUtilities.invokeLater(() -> {
            progress = Math.min(100, value);
            progressBar.setValue(progress);
            percentLabel.setText(progress + "%");
            statusLabel.setText(status);
        });
    }

    public void incrementProgress(int amount, String status) {
        setProgress(progress + amount, status);
    }

    public int getProgress() {
        return progress;
    }

    // ---------------- Custom Progress Bar ----------------

    static class ModernProgressBarUI extends javax.swing.plaf.basic.BasicProgressBarUI {
        @Override
        protected void paintDeterminate(Graphics g, JComponent c) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int width = progressBar.getWidth();
            int height = progressBar.getHeight();

            float percent = (float) progressBar.getPercentComplete();
            int fill = (int) (width * percent);

            // Background track
            g2.setColor(progressBar.getBackground());
            g2.fill(new RoundRectangle2D.Float(0, 0, width, height, height, height));

            // Progress fill
            g2.setColor(progressBar.getForeground());
            g2.fill(new RoundRectangle2D.Float(0, 0, fill, height, height, height));

            g2.dispose();
        }
    }
}
