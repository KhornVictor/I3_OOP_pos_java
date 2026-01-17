package main.com.pos.view.welcome;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.time.LocalTime;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import main.com.pos.components.ui.UI;
import main.com.pos.model.User;

public class WelcomePanel extends JPanel {

	private static final String BACKGROUND_PATH = "images/background/welcome/WelcomeBackground.png";
	private final JLabel greetingLabel;

	public WelcomePanel(User user) {
		setLayout(new BorderLayout());

		UI.ImageBackgroundPanel background = UI.setBackgroundImage(BACKGROUND_PATH);
		background.setLayout(new BorderLayout());

		JPanel overlay = new JPanel() {
			@Override
			protected void paintComponent(Graphics graphics) {
				super.paintComponent(graphics);
				Graphics2D g2 = (Graphics2D) graphics.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				GradientPaint gradient = new GradientPaint(0, 0, new Color(255, 255, 255, 110), 0, getHeight(), new Color(0, 0, 0, 40));
				g2.setPaint(gradient);
				g2.fillRect(0, 0, getWidth(), getHeight());
				g2.dispose();
			}
		};
		overlay.setOpaque(false);
		overlay.setLayout(new GridBagLayout());
		overlay.setBorder(new EmptyBorder(32, 32, 32, 32));

		JPanel content = new JPanel();
		content.setOpaque(false);
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

		greetingLabel = UI.setLabel(user.getRole().toUpperCase() + ", " + resolveGreeting(), new Font("Segoe UI", Font.BOLD, 56), new Color(61,61,61), SwingConstants.CENTER);
		JLabel subText = UI.setLabel("Welcome back to Point of Sale, " + user.getName(), new Font("Segoe UI", Font.PLAIN, 38), new Color(107, 107, 107), SwingConstants.CENTER);

		greetingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		subText.setAlignmentX(Component.CENTER_ALIGNMENT);

		content.add(greetingLabel);
		content.add(Box.createVerticalStrut(8));
		content.add(subText);

		GridBagConstraints gbc = UI.setGridBagConstraints(0, 0, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new java.awt.Insets(0, 0, 0, 0), 1.0, 1.0);
		overlay.add(content, gbc);

		background.add(overlay, BorderLayout.CENTER);
		add(background, BorderLayout.CENTER);
	}

	private String resolveGreeting() {
		int hour = LocalTime.now().getHour();
		if (hour >= 5 && hour < 10) return "Good Morning";
        if (hour >= 10 && hour < 13) return "Good Noon";
        if (hour >= 13 && hour < 17) return "Good Afternoon";
        if (hour >= 17 && hour < 22) return " Good Evening";
        return "Good Night";
	}
}