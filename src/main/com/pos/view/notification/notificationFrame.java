package main.com.pos.view.notification;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import javax.swing.*;
import main.com.pos.components.ui.UI.RoundedButton;
import main.com.pos.util.Telegram;

public final class notificationFrame extends JFrame {
    
    // Singleton instance
    private static notificationFrame instance;
    
    private JTextArea notificationArea;
    private JScrollPane scrollPane;
    private JPanel headerPanel;
    private JLabel telegramStatusLabel;
    private JLabel messageCountLabel;
    private String botToken;
    private String chatId;
    private int messageCount = 0;
    
    /**
     * Get singleton instance of notificationFrame
     */
    public static notificationFrame getInstance() {
        if (instance == null) {
            instance = new notificationFrame();
        }
        return instance;
    }
    
    public void notificationFrameInitialize() {
        // First-time initialization
        if (notificationArea == null) {
            setTitle("Notifications");
            setSize(800, 500);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            
            createUI();
            loadTelegramProperties();
            displayNotification("System", "Notification Frame initialized successfully");
            displayTelegramStatus();
        }
        
        // Show the frame (works for both first time and subsequent times)
        setVisible(true);
        toFront();
        requestFocus();
    }
    
    /**
     * Load Telegram configuration from telegram.properties
     */
    private void loadTelegramProperties() {
        Properties properties = new Properties();
        
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("main/com/pos/resources/telegram.properties")) {
            
            if (input == null) {
                botToken = "NOT_FOUND";
                chatId = "NOT_FOUND";
                displayNotification("System", "⚠️ telegram.properties file not found!");
                return;
            }
            
            properties.load(input);
            this.botToken = properties.getProperty("telegram.BotToken", "NOT_FOUND");
            this.chatId = properties.getProperty("telegram.ChatId", "NOT_FOUND");
            
            displayNotification("System", "Telegram configuration loaded successfully");
            
        } catch (IOException e) {
            displayNotification("Error", "Failed to load telegram.properties: " + e.getMessage());
        }
    }
    
    /**
     * Create UI components
     */
    private void createUI() {
        // Main panel with modern background
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(new Color(248, 249, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        // Header panel with Telegram status
        headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Content wrapper with padding
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(new Color(248, 249, 250));
        contentWrapper.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Notification area with modern styling
        notificationArea = new JTextArea();
        notificationArea.setEditable(false);
        notificationArea.setFont(new Font("Inter", Font.PLAIN, 13));
        if (!isFontAvailable("Inter")) {
            notificationArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        }
        notificationArea.setBackground(Color.WHITE);
        notificationArea.setForeground(new Color(33, 37, 41));
        notificationArea.setLineWrap(true);
        notificationArea.setWrapStyleWord(true);
        notificationArea.setMargin(new Insets(15, 15, 15, 15));
        
        // Scroll pane with modern border
        scrollPane = new JScrollPane(notificationArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(222, 226, 230), 1, true),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        
        contentWrapper.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(contentWrapper, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    /**
     * Create header panel with Telegram status
     */
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(13, 110, 253)); // Modern blue
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        
        // Left side - Title
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("System Notifications");
        titleLabel.setFont(new Font("Inter", Font.BOLD, 18));
        if (!isFontAvailable("Inter")) {
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        }
        titleLabel.setForeground(Color.WHITE);
        leftPanel.add(titleLabel);
        
        // Center - Telegram status
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        centerPanel.setOpaque(false);
        
        telegramStatusLabel = new JLabel();
        telegramStatusLabel.setFont(new Font("Inter", Font.PLAIN, 12));
        if (!isFontAvailable("Inter")) {
            telegramStatusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        }
        telegramStatusLabel.setForeground(new Color(255, 255, 255, 220));
        
        // Right side - Message count
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPanel.setOpaque(false);
        
        messageCountLabel = new JLabel("Total Messages: 0");
        messageCountLabel.setFont(new Font("Inter", Font.BOLD, 12));
        if (!isFontAvailable("Inter")) {
            messageCountLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
        }
        messageCountLabel.setForeground(Color.WHITE);
        messageCountLabel.setOpaque(true);
        messageCountLabel.setBackground(new Color(255, 255, 255, 30));
        messageCountLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 50), 1, true),
            BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));
        rightPanel.add(messageCountLabel);
        
        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(rightPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * Check if font is available in the system
     */
    private boolean isFontAvailable(String fontName) {
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for (String font : fonts) {
            if (font.equalsIgnoreCase(fontName)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Create button panel with actions
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 20, 15, 20));
        
        // Send Test Button - Modern green
        RoundedButton sendTestButton = new RoundedButton("Send Test Message", 8);
        sendTestButton.addActionListener(e -> sendTestTelegram());
        
        // Clear Button - Modern orange/warning
        RoundedButton clearButton = new RoundedButton("Clear", 8);
        clearButton.setBackground(new Color(255, 193, 7)); // Warning yellow
        clearButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        clearButton.setForeground(new Color(33, 37, 41)); // Dark text for yellow background
        clearButton.addActionListener(e -> {
            notificationArea.setText("");
            resetMessageCount();
        });
        
        // Close Button - Modern gray
        RoundedButton closeButton = new RoundedButton("Close", 8);
        closeButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        closeButton.setBackground(new Color(108, 117, 125)); // Gray
        closeButton.setForeground(Color.WHITE);
        closeButton.addActionListener(e -> setVisible(false)); // Hide instead of dispose
        
        // panel.add(sendTestButton);
        panel.add(clearButton);
        panel.add(closeButton);
        
        return panel;
    }
    
    public int getCountMessage() {
        return messageCount;
    }
    
    /**
     * Display notification in the text area
     */
    public synchronized void displayNotification(String source, String message) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String timestamp = now.format(formatter);
        
        messageCount++;
        String notification = String.format("[%s] %s: %s\n", timestamp, source, message);
        notificationArea.append(notification);
        
        // Update message count label
        if (messageCountLabel != null) {
            messageCountLabel.setText("Total Messages: " + messageCount);
        }
        
        // Auto-scroll to bottom
        JScrollBar vbar = scrollPane.getVerticalScrollBar();
        vbar.setValue(vbar.getMaximum());
    }
    
    /**
     * Display Telegram connection status
     */
    private void displayTelegramStatus() {
        if ("NOT_FOUND".equals(botToken) || "NOT_FOUND".equals(chatId)) {
            telegramStatusLabel.setText("⚠️ Telegram Status: NOT CONFIGURED");
            telegramStatusLabel.setForeground(new Color(255, 193, 7)); // Warning yellow
        } else {
            String statusText = String.format("✅ Telegram Status: Connected | Chat ID: %s", chatId);
            telegramStatusLabel.setText(statusText);
            telegramStatusLabel.setForeground(new Color(255, 255, 255, 220));
        }
    }
    
    private void sendTestTelegram() {
        if ("NOT_FOUND".equals(botToken) || "NOT_FOUND".equals(chatId)) {
            displayNotification("Error", "❌ Telegram is not configured. Check telegram.properties");
            return;
        }
        
        String testMessage = "🧪 Test message from POS Notification System - " + 
                           LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        Telegram.telegramSend(testMessage);
        displayNotification("Telegram", "Test message sent to Chat ID: " + chatId);
    }
    
    public void sendViaBot(String message) {
        if ("NOT_FOUND".equals(botToken) || "NOT_FOUND".equals(chatId)) {
            displayNotification("Error", "❌ Cannot send via Telegram: Configuration missing");
            return;
        }
        
        Telegram.telegramSend(message);
        displayNotification("Telegram Bot", message);
    }
    
    /**
     * Get Telegram Bot Token
     */
    public String getBotToken() {
        return botToken;
    }
    
    /**
     * Get Telegram Chat ID
     */
    public String getChatId() {
        return chatId;
    }
    
    /**
     * Get the total count of messages displayed
     */
    public int getMessageCount() {
        return messageCount;
    }
    
    /**
     * Reset the message counter
     */
    public void resetMessageCount() {
        messageCount = 0;
        if (messageCountLabel != null) {
            messageCountLabel.setText("Total Messages: 0");
        }
    }
}