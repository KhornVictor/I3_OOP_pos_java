package main.com.pos.components.ui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import org.w3c.dom.events.MouseEvent;


public class UI extends JFrame {
    public static Image setApplicationIcon( String iconPath) {
        try {
            Image image = ImageIO.read(UI.class.getClassLoader().getResourceAsStream(iconPath));
            return image;
        } catch (IOException e) { System.err.println("❌ Failed to load app icon."); }
        return null;
    }

    public static ImageBackgroundPanel setBackgroundImage(String imagePath) {
        ImageBackgroundPanel backgroundPanel = new ImageBackgroundPanel("src/main/com/pos/resources/" + imagePath);
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

    public static Image getImage(String imagePath) {
        try {
            File file = new File("src/main/com/pos/resources/" + imagePath);
            Image image = ImageIO.read(file);
            return image;
        } catch (IOException e) {
            System.err.println("❌ Failed to load image: " + imagePath);
        }
        return null;    
    }

    public static Image internetImage(String imageUrl) {
        try {
            Image image = ImageIO.read(new URL(imageUrl));
            return image;
        } catch (IOException e) {
            System.err.println("❌ Failed to load image from URL: " + imageUrl);
        }
        return null;
    }

    public static JLabel setIconLabel(String iconPath, int width, int height) {
        JLabel iconLabel = new JLabel();
        java.io.InputStream is = null;
        String path = "src/main/com/pos/resources/" + iconPath;
        try {
            File file = new File(path);
            is = new FileInputStream(file);
            Image image = ImageIO.read(is);
            Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            iconLabel.setIcon(new ImageIcon(scaledImage));
        } catch (IOException | NullPointerException e) {
            System.err.println("❌ Failed to load icon: " + path);
        } finally {
            if (is != null) {
                try { is.close(); } catch (IOException e) { /* Ignore */ }
            }
        }
        return iconLabel;
    }


    public static JLabel setInternetIconLabel(String imageUrl, int width, int height) {
        JLabel iconLabel = new JLabel();
        if (imageUrl != null) {
            Image image = internetImage(imageUrl.trim());
            Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            iconLabel.setIcon(new javax.swing.ImageIcon(scaledImage));
        }
        return iconLabel;
    }

    public static JPanel setInternetIconPanel(String imageUrl, int width, int height) {
        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Image image = internetImage(imageUrl);
                if (image != null) {
                    g2.drawImage(image, 0, 0, width, height, this);
                } else {
                    System.err.println("❌ Failed to load internet icon: " + imageUrl);
                }

                g2.dispose();
            }
        };
        iconPanel.setOpaque(false);
        iconPanel.setPreferredSize(new Dimension(width, height));
        return iconPanel;
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
            try { backgroundImage = ImageIO.read(new File(imagePath)); }
            catch (IOException e) { System.err.println("❌ Failed to load background image: " + imagePath); }
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
        private final Color normalBackgroundColor;
        private final Color hoverBackgroundColor;
        private final Color activeBackgroundColor;
        private final Color pressedBackgroundColor;
        private final Color fColor;
        private final Color fColored;


        public SidebarMenuButton(String url, JLabel textLabel,Color fColor,Color fColored, Color normalBackgroundColor, Color hoverBackgroundColor, Color activeBackgroundColor, Color pressedBackgroundColor) {
            this.iconLabel = UI.setInternetIconLabel(url, 24, 24);
            this.textLabel = textLabel;
            this.normalBackgroundColor = normalBackgroundColor;
            this.hoverBackgroundColor = hoverBackgroundColor;
            this.activeBackgroundColor = activeBackgroundColor;
            this.pressedBackgroundColor = pressedBackgroundColor;
            this.fColor = fColor;
            this.fColored = fColored;

            setLayout(new BorderLayout());
            setOpaque(true);
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
                textLabel.setForeground(fColor);
                iconLabel.setForeground(fColor);
            } else {
                textLabel.setForeground(fColored);
                iconLabel.setForeground(fColored);
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
                g2.setColor(activeBackgroundColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);

            } else if (model.isPressed()) {
                g2.setColor(pressedBackgroundColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);

            } else if (model.isRollover()) {
                g2.setColor(hoverBackgroundColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
            } else {
                g2.setColor(normalBackgroundColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static GradientCard actionCard(String title, String desc, Color color, String icon, MouseAdapter mouseAdapter) {
        return actionCardWithImage(title, desc, null, icon, mouseAdapter);
    }

    public static GradientCard actionCardWithImage(String title, String desc, String imageUrl, String icon, MouseAdapter mouseAdapter) {
        GradientCard card = new GradientCard(null);
        if (imageUrl != null && !imageUrl.isEmpty()) card.setBackgroundImage(imageUrl);
        else card.setBackgroundColor(new Color(100, 150, 200));

        JLabel iconLbl = new JLabel(setIconLabel(icon , 28, 28).getIcon());
        iconLbl.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        iconLbl.setHorizontalAlignment(JLabel.CENTER);
        iconLbl.setPreferredSize(new Dimension(50, 50));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLbl.setForeground(Color.WHITE);

        JLabel descLbl = new JLabel(desc);
        descLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        descLbl.setForeground(new Color(255, 255, 255, 180));

        card.add(iconLbl);
        card.add(Box.createVerticalStrut(10));
        card.add(titleLbl);
        card.add(descLbl);

        if (mouseAdapter != null) {
            card.addMouseListener(mouseAdapter);
        }

        addHover(card);

        return card;
    }

    public static void addHover(GradientCard card) {
        card.addMouseListener(new MouseAdapter() {
            @SuppressWarnings("unused")
            public void mouseEntered(MouseEvent e) {
                card.setBrightness(1.15f);
                card.repaint();
            }

            @SuppressWarnings("unused")
            public void mouseExited(MouseEvent e) {
                card.setBrightness(1.0f);
                card.repaint();
            }
        });
    }
    
    public static class GradientCard extends JPanel {
        private final Color baseColor;
        private Image backgroundImage;
        private float brightness = 1.0f;
        private static final int RADIUS = 15;
        private static final int SHADOW_SIZE = 8;

        public GradientCard(Color baseColor) {
            this.baseColor = baseColor;
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(new EmptyBorder(20, 20, 20, 20));
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        public void setBackgroundImage(String imageUrl) {
            try {
                File file = new File("src/main/com/pos/resources/" + imageUrl);
                this.backgroundImage = ImageIO.read(file);
            } catch (IOException e) {
                System.err.println("❌ Failed to load image: " + imageUrl);
            }
        }

        public void setBackgroundColor(Color color) {
            // This would require making baseColor non-final, or we can update the color logic
        }

        public void setBrightness(float brightness) {
            this.brightness = brightness;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            // Draw shadow
            drawShadow(g2d, width, height);

            // Draw rounded rectangle with gradient or image
            RoundRectangle2D.Float shape = new RoundRectangle2D.Float(
                SHADOW_SIZE / 2, SHADOW_SIZE / 2,
                width - SHADOW_SIZE, height - SHADOW_SIZE,
                RADIUS, RADIUS
            );

            if (backgroundImage != null) {
                // Draw background image
                g2d.drawImage(backgroundImage, 
                    (int)(SHADOW_SIZE / 2), (int)(SHADOW_SIZE / 2),
                    (int)(width - SHADOW_SIZE), (int)(height - SHADOW_SIZE), 
                    this);
                
                // Add a dark overlay for better text visibility
                g2d.setColor(new Color(0, 0, 0, 100));
                g2d.fill(shape);
            } else if (baseColor != null) {
                // Create gradient
                Color color1 = brighten(baseColor, brightness);
                Color color2 = brighten(darken(baseColor, 0.2f), brightness);

                GradientPaint gradient = new GradientPaint(
                        0, 0, color1,
                        0, height, color2
                );
                g2d.setPaint(gradient);
                g2d.fill(shape);
            }
            
            // Draw rounded border
            g2d.setColor(new Color(255, 255, 255, 50));
            g2d.setStroke(new BasicStroke(1));
            g2d.drawRoundRect((int)(SHADOW_SIZE / 2), (int)(SHADOW_SIZE / 2),
                    (int)(width - SHADOW_SIZE - 1), (int)(height - SHADOW_SIZE - 1),
                    RADIUS, RADIUS);
        }

        private void drawShadow(Graphics2D g2d, int width, int height) {
            g2d.setColor(new Color(0, 0, 0, 30));
            RoundRectangle2D.Float shadowShape = new RoundRectangle2D.Float(
                    SHADOW_SIZE / 2 + 2, SHADOW_SIZE / 2 + 2,
                    width - SHADOW_SIZE - 4, height - SHADOW_SIZE - 4,
                    RADIUS, RADIUS
            );
            g2d.fill(shadowShape);
        }

        private Color brighten(Color color, float factor) {
            return new Color(
                    Math.min(255, (int) (color.getRed() * factor)),
                    Math.min(255, (int) (color.getGreen() * factor)),
                    Math.min(255, (int) (color.getBlue() * factor)),
                    color.getAlpha()
            );
        }

        private Color darken(Color color, float factor) {
            return new Color(
                    (int) (color.getRed() * (1 - factor)),
                    (int) (color.getGreen() * (1 - factor)),
                    (int) (color.getBlue() * (1 - factor)),
                    color.getAlpha()
            );
        }
    }

     public static GradientCard statisticCard(String value, String title, String percent, Color color, String icon) {
        GradientCard card = new GradientCard(color);

        JLabel iconLbl = new JLabel(setIconLabel(icon , 32, 32).getIcon());
        iconLbl.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        iconLbl.setHorizontalAlignment(JLabel.CENTER);
        iconLbl.setPreferredSize(new Dimension(50, 50));

        JLabel valueLbl = new JLabel(value);
        valueLbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valueLbl.setForeground(Color.WHITE);

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLbl.setForeground(new Color(255, 255, 255, 200));

        JLabel percentLbl = new JLabel(percent);
        percentLbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        percentLbl.setForeground(
            percent.startsWith("-") ? new Color(255, 100, 100) : new Color(100, 255, 100)
        );

        card.add(iconLbl);
        card.add(Box.createVerticalStrut(10));
        card.add(valueLbl);
        card.add(titleLbl);
        card.add(Box.createVerticalStrut(5));
        card.add(percentLbl);

        addHover(card);

        return card;
    }

     public static Component actionCardWithImage(String title, String desc, String imageUrl, String icon, Object mouseAdapter) {
        throw new UnsupportedOperationException("Unimplemented method 'actionCardWithImage'");
     }
}