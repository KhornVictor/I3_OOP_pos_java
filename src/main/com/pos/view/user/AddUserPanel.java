package main.com.pos.view.user;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import main.com.pos.dao.AddressDAO;
import main.com.pos.dao.UserDAO;
import main.com.pos.model.Address;
import main.com.pos.model.User;
import main.com.pos.util.DragDropImage;
import main.com.pos.util.Telegram;

public class AddUserPanel extends JPanel {
    
    @SuppressWarnings("unused")
    private JPanel contentPanel;
    private DragDropImage imagePanel;
    private JTextField nameField;
    private JTextField usernameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JPasswordField passwordField;
    private JComboBox<String> roleCombo;
    private JTextField streetField;
    private JTextField cityField;
    private JTextField stateField;
    private JTextField zipCodeField;
    private JTextField countryField;
    private JButton saveButton;
    private JButton cancelButton;
    private final JPanel formPanel;
    private Runnable onUserAddedCallback;

    
    public AddUserPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(350, Integer.MAX_VALUE));
        setBackground(new Color(250, 251, 253));
        
        formPanel = new JPanel();
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createContentPanel(), BorderLayout.CENTER);
        add(createActionsPanel(), BorderLayout.SOUTH);
    }
    
    /* ==================== SET CALLBACK ==================== */
    public void setOnUserAddedCallback(Runnable callback) {
        this.onUserAddedCallback = callback;
    }   
    
    /* ==================== HEADER PANEL ==================== */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(59, 130, 246), 0, getHeight(), new Color(37, 99, 235));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        headerPanel.setPreferredSize(new Dimension(350, 60));
        headerPanel.setLayout(new BorderLayout());
        
        JLabel titleLabel = new JLabel("Add New User");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        headerPanel.add(titleLabel, BorderLayout.CENTER);
            
        return headerPanel;
    }
    
    /* ==================== CONTENT PANEL ==================== */
    private JScrollPane createContentPanel() {
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(new Color(250, 251, 253));
        mainContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Image Panel (top)
        imagePanel = createImagePanel();
        imagePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContent.add(imagePanel);

        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));

        // Form Panel
        JPanel formPanel = createFormPanel();
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContent.add(formPanel);

        // Push content to top
        mainContent.add(Box.createVerticalGlue());

        // Wrap in scroll pane
        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Scrollbar styling
        JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
        scrollBar.setPreferredSize(new Dimension(8, 0));
        scrollBar.setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                thumbColor = new Color(195, 205, 215);
                trackColor = new Color(245, 247, 250);
            }
        });

        return scrollPane;
    }

    
    /* ==================== IMAGE PANEL ==================== */
    private DragDropImage createImagePanel() {
        DragDropImage panel = new DragDropImage(290, 180, "avatar_default.png");
        panel.setOpaque(false);
        // Size aligned with requested drag-drop dimensions
        panel.setPreferredSize(new Dimension(290, 180));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        return panel;
    }
    
    /* ==================== FORM PANEL ==================== */
    private JPanel createFormPanel() {

        formPanel.setOpaque(false);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        
        // User form fields
        formPanel.add(createFormField("Full Name", "nameField"));
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(createFormField("Username", "usernameField"));
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(createFormField("Email", "emailField"));
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(createFormField("Phone", "phoneField"));
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(createFormField("Password", "passwordField"));
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(createRoleFieldCompact());
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Address section
        JLabel addressSectionLabel = new JLabel("Address (Optional)");
        addressSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addressSectionLabel.setForeground(new Color(31, 41, 55));
        addressSectionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(addressSectionLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        formPanel.add(createFormField("Street", "streetField"));
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(createFormField("City", "cityField"));
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(createFormField("State", "stateField"));
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(createFormField("Zip Code", "zipCodeField"));
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(createFormField("Country", "countryField"));
        
        formPanel.add(Box.createVerticalGlue());

        return formPanel;
    }
    
    /* ==================== CREATE FORM FIELD ==================== */
    private JPanel createFormField(String label, String fieldType) {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setOpaque(false);    
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
        fieldPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));
        
        // Label
        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        fieldLabel.setForeground(new Color(31, 41, 55));
        fieldLabel.setHorizontalAlignment(SwingConstants.LEFT);
        fieldLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldPanel.add(fieldLabel);
        fieldPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        
        if (null != fieldType) 
        switch (fieldType) {
            case "nameField" -> {
                nameField = createModernTextField();
                nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
                fieldPanel.add(nameField);
            }
            case "usernameField" -> {
                usernameField = createModernTextField();
                usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
                fieldPanel.add(usernameField);
            }
            case "emailField" -> {
                emailField = createModernTextField();
                emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
                fieldPanel.add(emailField);
            }
            case "phoneField" -> {
                phoneField = createModernTextField();
                phoneField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
                fieldPanel.add(phoneField);
            }
            case "passwordField" -> {
                passwordField = new JPasswordField();
                passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                passwordField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(209, 213, 219), 1, true),
                        BorderFactory.createEmptyBorder(10, 14, 10, 14)
                )); passwordField.setBackground(Color.WHITE);
                passwordField.setForeground(new Color(55, 65, 81));
                passwordField.setCaretColor(new Color(59, 130, 246));
                passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
                fieldPanel.add(passwordField);
            }
            case "streetField" -> {
                streetField = createModernTextField();
                streetField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
                fieldPanel.add(streetField);
            }
            case "cityField" -> {
                cityField = createModernTextField();
                cityField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
                fieldPanel.add(cityField);
            }
            case "stateField" -> {
                stateField = createModernTextField();
                stateField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
                fieldPanel.add(stateField);
            }
            case "zipCodeField" -> {
                zipCodeField = createModernTextField();
                zipCodeField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
                fieldPanel.add(zipCodeField);
            }
            case "countryField" -> {
                countryField = createModernTextField();
                countryField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
                fieldPanel.add(countryField);
            }
            default ->  fieldPanel.add(new JLabel("Unknown field type"));
        } return fieldPanel;
    }
    
    /* ==================== MODERN TEXT FIELD ==================== */
    private JTextField createModernTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1, true),
            BorderFactory.createEmptyBorder(10, 14, 10, 14)
        ));
        field.setBackground(Color.WHITE);
        field.setForeground(new Color(55, 65, 81));
        field.setCaretColor(new Color(59, 130, 246));
        
        field.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!field.hasFocus()) {
                    field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(156, 163, 175), 1, true),
                        BorderFactory.createEmptyBorder(10, 14, 10, 14)
                    ));
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (!field.hasFocus()) {
                    field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(209, 213, 219), 1, true),
                        BorderFactory.createEmptyBorder(10, 14, 10, 14)
                    ));
                }
            }
        });
        
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(59, 130, 246), 2, true),
                    BorderFactory.createEmptyBorder(10, 14, 10, 14)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(209, 213, 219), 1, true),
                    BorderFactory.createEmptyBorder(10, 14, 10, 14)
                ));
            }
        });
        
        return field;
    }
    
    /* ==================== CREATE ROLE FIELD (COMPACT) ==================== */
    private JPanel createRoleFieldCompact() {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setOpaque(false);
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
        fieldPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel fieldLabel = new JLabel("Role");
        fieldLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        fieldLabel.setForeground(new Color(31, 41, 55));
        fieldLabel.setHorizontalAlignment(SwingConstants.LEFT);
        fieldLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldPanel.add(fieldLabel);
        fieldPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        
        // User roles
        String[] roles = {"Select Role", "Admin", "Cashier", "Manager", "Staff"};
        roleCombo = new JComboBox<>(roles);
        roleCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        roleCombo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1, true),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        roleCombo.setBackground(Color.WHITE);
        roleCombo.setForeground(new Color(55, 65, 81));
        roleCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        roleCombo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        roleCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);
                label.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                
                if (isSelected) {
                    label.setBackground(new Color(59, 130, 246));
                    label.setForeground(Color.WHITE);
                } else {
                    label.setBackground(Color.WHITE);
                    label.setForeground(new Color(55, 65, 81));
                }
                
                return label;
            }
        });
        
        fieldPanel.add(roleCombo);
        
        return fieldPanel;
    }
    
    private JPanel createActionsPanel() {
        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 12, 20));
        actionsPanel.setBackground(new Color(250, 251, 253));
        actionsPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(215, 220, 228)));
        
        // Cancel Button
        cancelButton = createModernButton("Cancel", new Color(229, 231, 235), new Color(107, 114, 128));
        cancelButton.addActionListener(e -> clearForm());
        
        // Save Button
        saveButton = createModernButton("Save User", new Color(34, 139, 34), Color.WHITE);
        saveButton.addActionListener(e -> saveUser());
        
        actionsPanel.add(cancelButton);
        actionsPanel.add(saveButton);
        
        return actionsPanel;
    }
    
    /* ==================== CREATE MODERN BUTTON ==================== */
    private JButton createModernButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setBorder(BorderFactory.createEmptyBorder(11, 28, 11, 28));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            private final Color originalBg = bgColor;
            
            @Override
            public void mouseEntered(MouseEvent e) {
                int r = Math.min(255, originalBg.getRed() + 20);
                int g = Math.min(255, originalBg.getGreen() + 20);
                int b = Math.min(255, originalBg.getBlue() + 20);
                button.setBackground(new Color(r, g, b));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(originalBg);
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                int r = Math.max(0, originalBg.getRed() - 20);
                int g = Math.max(0, originalBg.getGreen() - 20);
                int b = Math.max(0, originalBg.getBlue() - 20);
                button.setBackground(new Color(r, g, b));
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(originalBg);
            }
        });
        
        return button;
    }
    
    /* ==================== SAVE USER ==================== */
    private void saveUser() {
        String name = nameField.getText().trim();
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String role = (String) roleCombo.getSelectedItem();
        String street = streetField.getText().trim();
        String city = cityField.getText().trim();
        String state = stateField.getText().trim();
        String zipCode = zipCodeField.getText().trim();
        String country = countryField.getText().trim();
        
        if (name.isEmpty()) {
            showError("Full name is required!");
            return;
        }
        
        if (username.isEmpty()) {
            showError("Username is required!");
            return;
        }
        
        if (email.isEmpty()) {
            showError("Email is required!");
            return;
        }
        
        if (password.isEmpty()) {
            showError("Password is required!");
            return;
        }
        
        if ("Select Role".equals(role)) {
            showError("Please select a valid role!");
            return;
        }
        
        try {
            User newUser = new User();
            newUser.setUserId(0);
            newUser.setUsername(username);
            newUser.setName(name);
            newUser.setEmail(email);
            newUser.setPassword(password);
            newUser.setRole(role);
            imagePanel.saveImageToResources(username + ".png");
            newUser.setImage("/images/avatar/" + username + ".png");
            int addressId = 0;
            if (!street.isEmpty() || !city.isEmpty() || !state.isEmpty() || !zipCode.isEmpty() || !country.isEmpty()) {
                Address addressObj = new Address(1, street, city, state, zipCode, country);
                AddressDAO addressDAO = new AddressDAO();
                if (addressDAO.create(addressObj)) {
                    addressId = addressObj.getAddressId();
                    newUser.setAddressId(addressId);
                }
            }
            
            // Then create user with address reference
            UserDAO userDAO = new UserDAO();
            if (userDAO.create(newUser)) {
                showSuccess("User added successfully!");
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("👤 New User Added:\n\n");
                stringBuilder.append("Name: ");
                stringBuilder.append(newUser.getName());
                stringBuilder.append("\n");
                stringBuilder.append("Username: ");
                stringBuilder.append(username);
                stringBuilder.append("\n");
                stringBuilder.append("Email: ");
                stringBuilder.append(newUser.getEmail());
                stringBuilder.append("\n");
                stringBuilder.append("Phone: ");
                stringBuilder.append(phone);
                stringBuilder.append("\n");
                stringBuilder.append("Role: ");
                stringBuilder.append(newUser.getRole());
                stringBuilder.append("\n");
                if (!street.isEmpty() || !city.isEmpty() || !state.isEmpty() || !zipCode.isEmpty() || !country.isEmpty()) {
                    stringBuilder.append("Address: ");
                    stringBuilder.append(street);
                    if (!city.isEmpty()) stringBuilder.append(", ").append(city);
                    if (!state.isEmpty()) stringBuilder.append(", ").append(state);
                    if (!zipCode.isEmpty()) stringBuilder.append(" ").append(zipCode);
                    if (!country.isEmpty()) stringBuilder.append(", ").append(country);          
                    stringBuilder.append("\n");
                }
                Telegram.telegramSend(stringBuilder.toString());
                clearForm();
                if (onUserAddedCallback != null) onUserAddedCallback.run();
            } else  showError("Failed to add user. Please try again.");
        } catch (IllegalArgumentException e) {
            showError("Invalid input: " + e.getMessage());
        }
    }
    
    /* ==================== CLEAR FORM ==================== */
    private void clearForm() {
        nameField.setText("");
        usernameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        passwordField.setText("");
        roleCombo.setSelectedIndex(0);
        streetField.setText("");
        cityField.setText("");
        stateField.setText("");
        zipCodeField.setText("");
        countryField.setText("");
        imagePanel.resetImage();
        imagePanel.repaint();
        nameField.requestFocus();
    }
    
    /* ==================== SHOW MESSAGES ==================== */
    private void showSuccess(String message) {
         JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Add User Panel Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 600);
        frame.add(new AddUserPanel());
        frame.setVisible(true);
    }
}