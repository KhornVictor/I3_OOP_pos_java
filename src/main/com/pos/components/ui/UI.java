package main.com.pos.components.ui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;


public class UI extends JFrame {
    public static Image setApplicationIcon( String iconPath) {
        try {
            Image image = ImageIO.read(UI.class.getClassLoader().getResourceAsStream(iconPath));
            return image;
        } catch (IOException e) { System.err.println("❌ Failed to load app icon."); }
        return null;
    }

    public static ImageBackgroundPanel setBackgroundImage(String imagePath) {
        ImageBackgroundPanel backgroundPanel = new ImageBackgroundPanel(imagePath);
        backgroundPanel.setLayout(new GridBagLayout());
        backgroundPanel.setOpaque(true);
        return backgroundPanel;
    }

    public static JPanel spacerPanel(Dimension size, boolean opaque) {
        JPanel spacer = new JPanel(new GridBagLayout());
        spacer.setPreferredSize(size);
        spacer.setOpaque(opaque);
        return spacer;
    }

    public static JPanel cardPanel(int radius, Color fill, Color stroke, Dimension size, Border border) {
        JPanel card = new RoundedPanel(radius, fill, stroke, size, border);
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(size);
        card.setBorder(border);
        return card;
    }

    public static JLabel setIconLabel(String iconPath, int width, int height) {
        JLabel iconLabel = new JLabel();
        java.io.InputStream is = null;
        try {
            is = UI.class.getClassLoader().getResourceAsStream(iconPath);
            if (is == null) {
                System.err.println("❌ Icon not found on classpath: " + iconPath);
                return iconLabel;
            }
            Image image = ImageIO.read(is);
            if (image != null) {
                Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                iconLabel.setIcon(new javax.swing.ImageIcon(scaledImage));
            }
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("❌ Failed to load icon image: " + iconPath);
        } finally {
            if (is != null) {
                try { is.close(); } catch (IOException ignored) {}
            }
        }
        return iconLabel;
    }

    // GridBagConstraints object, which tells GridBagLayout how a component should be placed inside a container.
    public static GridBagConstraints setGridBagConstraints(
        int gridx, int gridy, int gridwidth, int gridheight,
        int anchor, int fill, Insets insets,
        double weightx, double weighty
    ) {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridheight = gridheight;
        gridBagConstraints.gridwidth = gridwidth;
        gridBagConstraints.weighty = weighty;
        gridBagConstraints.weightx = weightx;
        gridBagConstraints.anchor = anchor;
        gridBagConstraints.insets = insets;
        gridBagConstraints.gridx = gridx;
        gridBagConstraints.gridy = gridy;
        gridBagConstraints.fill = fill;
        return gridBagConstraints;
    }

    public static JLabel setLabel(String text, Font font, Color color, int horizontalAlignment){
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        label.setHorizontalAlignment(horizontalAlignment);
        return label;
    }

    public static JButton setButton(String text, Font font, Color backgroundColor, Color foregroundColor, Border padding, int borderRadius, ActionListener actionListener) {
        JButton button = new RoundedButton(text, borderRadius);
        button.setFont(font);
        button.setBackground(backgroundColor);
        button.setForeground(foregroundColor);
        button.setFocusPainted(false);
        button.setBorder(padding);
        button.addActionListener(actionListener);
        return button;
    }

    public static JTextField setTextInput(Font font, Color backgroundColor, Color caretColor, Color foregroundColor, Border border) {
        JTextField input = new JTextField();
        input.setFont(font);
        input.setBackground(backgroundColor);
        input.setForeground(foregroundColor);
        input.setCaretColor(caretColor);
        input.setBorder(border);
        return input;
    }

    public static JPasswordField setPasswordInput(Font font, Color backgroundColor, Color caretColor, Color foregroundColor, Border border) {
        JPasswordField input = new JPasswordField();
        input.setFont(font);
        input.setBackground(backgroundColor);
        input.setForeground(foregroundColor);
        input.setCaretColor(caretColor);
        input.setBorder(border);
        return input;
    }

    public static class ImageBackgroundPanel extends JPanel {
        private Image backgroundImage;
        @SuppressWarnings("unused")
        public ImageBackgroundPanel(String imagePath) {
            try { backgroundImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream(imagePath));  } 
            catch (IOException | NullPointerException e) { System.err.println("❌ Failed to load background image: " + imagePath); }
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            if (backgroundImage != null) {
                Graphics2D graphics2d = (Graphics2D) graphics.create();
                graphics2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                graphics2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                graphics2d.dispose();
            }
        }
    }

    public static class RoundedPanel extends JPanel {
        private final int radius;
        private final Color fill;
        private final Color stroke;

        public RoundedPanel(int radius, Color fill, Color stroke, Dimension size, Border border) {
            this.radius = radius;
            this.fill = fill;
            this.stroke = stroke;
            setOpaque(false);
            setLayout(new BorderLayout());
            setPreferredSize(size);
            setBorder(border);

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

    public static class RoundedButton extends JButton {
        private final int radius;

        public RoundedButton(String text, int radius) {
            super(text);
            this.radius = radius;
            setContentAreaFilled(false);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius * 2, radius * 2);
            super.paintComponent(graphics);
            g2.dispose();
        }
    }

    public static class SidebarMenuButton extends JButton {

        private boolean active = false;
        private final JLabel iconLabel;
        private final JLabel textLabel;

        public SidebarMenuButton(JLabel iconLabel, JLabel textLabel) {
            this.iconLabel = iconLabel;
            this.textLabel = textLabel;

            setLayout(new BorderLayout());
            setOpaque(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setRolloverEnabled(true);

            JPanel inner = new JPanel(new BorderLayout());
            inner.setOpaque(false);
            inner.add(iconLabel, BorderLayout.WEST);
            inner.add(textLabel, BorderLayout.CENTER);
            add(inner, BorderLayout.CENTER);

            updateColors();
        }

        public void setActive(boolean value) {
            active = value;
            updateColors();
            repaint();
        }

        private void updateColors() {
            if (active) {
                textLabel.setForeground(Color.WHITE);
                iconLabel.setForeground(Color.WHITE);
            } else {
                textLabel.setForeground(new Color(30, 41, 59));
                iconLabel.setForeground(new Color(30, 41, 59));
            }
        }

        @Override
        protected void paintComponent(java.awt.Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(
                    java.awt.RenderingHints.KEY_ANTIALIASING,
                    java.awt.RenderingHints.VALUE_ANTIALIAS_ON
            );

            if (active) {
                g2.setColor(new Color(59, 130, 246));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);

            } else if (model.isPressed()) {
                g2.setColor(new Color(203, 213, 225));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);

            } else if (model.isRollover()) {
                g2.setColor(new Color(226, 232, 240));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }


}