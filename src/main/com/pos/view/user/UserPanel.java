package main.com.pos.view.user;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;
import main.com.pos.components.ui.TableUser;
import main.com.pos.components.ui.UI;
import main.com.pos.dao.UserDAO;
import main.com.pos.model.User;

public class UserPanel extends JPanel {
    
    private final UserDAO userDAO;
    private final List<User> allUsers;
    private List<User> currentFilteredUsers;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private final UserDetailPanel userDetail;
    private final AddUserPanel addUserPanel;
    private final JPanel centerPanel;
    private final JPanel sidePanel;
    private User selectedUser;
    
    private String currentStatusFilter = "All Status";
    private String currentRoleFilter = "All Roles";

    public UserPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(245, 247, 250));
        setVisible(true);

        userDAO = new UserDAO();
        allUsers = userDAO.getAll();
        currentFilteredUsers = new ArrayList<>(allUsers);
        selectedUser = currentFilteredUsers.isEmpty() ? null : currentFilteredUsers.get(0);
        
        // Initialize components first before using them
        userDetail = new UserDetailPanel(selectedUser);
        addUserPanel = new AddUserPanel();
        sidePanel = createSidePanel();

        add(createTopBar(), BorderLayout.NORTH);
        centerPanel = new JPanel(new BorderLayout(10, 0));
        centerPanel.setOpaque(false);
        centerPanel.add(sidePanel, BorderLayout.EAST);
        centerPanel.add(createTable(), BorderLayout.CENTER);
        
        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createSidePanel(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(userDetail, BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(350, getHeight()));
        panel.setBackground(new Color(245, 247, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        return panel;
    }

    private JPanel createTopBar() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(245, 247, 250));
        
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        left.setBackground(new Color(245, 247, 250));
        
        JPanel searchPanel = createSearchBar();
        left.add(searchPanel);
        
        JComboBox<String> statusBox = createStyledComboBox(
            new String[]{"All Status", "Active", "Inactive", "Banned"}
        );
        statusBox.addActionListener(e -> {
            currentStatusFilter = statusBox.getSelectedItem().toString();
            applyFilters();
        });
        left.add(statusBox);

        JComboBox<String> roleBox = createStyledComboBox(
            new String[]{"All Roles", "Admin", "Staff", "Customer"}
        );
        roleBox.addActionListener(e -> {
            currentRoleFilter = roleBox.getSelectedItem().toString();
            applyFilters();
        });
        left.add(roleBox);

        JButton deleteButton = createAnimatedButton(
            "Delete", 
            new Color(255, 90, 90), 
            new Color(220, 50, 50), 
            new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    handleDeleteUser();
                }
            }
        );
        
        JButton updateButton = createAnimatedButton(
            "Update", 
            new Color(100, 149, 237), 
            new Color(70, 119, 207), 
            new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    handleUpdateUser();
                }
            }
        );
        
        JButton addButton = createAnimatedButton(
            "Add User", 
            new Color(34, 139, 34), 
            new Color(24, 109, 24), 
            new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    handleAddUser();
                }
            }
        );

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setBackground(new Color(245, 247, 250));
        right.add(deleteButton);
        right.add(updateButton);    
        right.add(addButton);

        panel.add(left, BorderLayout.WEST);
        panel.add(right, BorderLayout.EAST);

        return panel;
    }

    private JPanel createSearchBar() {
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        searchPanel.setPreferredSize(new Dimension(280, 38));

        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        searchIcon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));

        JTextField searchField = new JTextField();
        searchField.setFont(new Font("Arial", Font.PLAIN, 13));
        searchField.setBorder(BorderFactory.createEmptyBorder());
        searchField.setBackground(Color.WHITE);
        searchField.setForeground(new Color(60, 60, 60));
        searchField.setCaretColor(new Color(60, 60, 60));
         
        searchField.setText("Search users...");
        searchField.setForeground(Color.GRAY);
        
        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Search users...")) {
                    searchField.setText("");
                    searchField.setForeground(new Color(60, 60, 60));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search users...");
                    searchField.setForeground(Color.GRAY);
                }
            }
        });

        JButton clearButton = new JButton("✕");
        clearButton.setFont(new Font("Arial", Font.PLAIN, 14));
        clearButton.setBorder(BorderFactory.createEmptyBorder());
        clearButton.setBackground(Color.WHITE);
        clearButton.setForeground(Color.GRAY);
        clearButton.setFocusPainted(false);
        clearButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearButton.setVisible(false);
        clearButton.addActionListener(e -> {
            searchField.setText("");
            searchField.requestFocus();
        });
        
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterUsers(searchField.getText()); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterUsers(searchField.getText()); }
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterUsers(searchField.getText()); }
        });

        searchPanel.add(searchIcon, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(clearButton, BorderLayout.EAST);

        return searchPanel;
    }

    private JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setFont(new Font("Arial", Font.PLAIN, 13));
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(new Color(60, 60, 60));
        comboBox.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        comboBox.setPreferredSize(new Dimension(150, 38));
        comboBox.setFocusable(true);
        comboBox.setCursor(new Cursor(Cursor.HAND_CURSOR));

        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);
                
                label.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                label.setFont(new Font("Arial", Font.PLAIN, 13));
                
                if (isSelected) {
                    label.setBackground(new Color(100, 149, 237));
                    label.setForeground(Color.WHITE);
                } else {
                    label.setBackground(Color.WHITE);
                    label.setForeground(new Color(60, 60, 60));
                }
                return label;
            }
        });
        return comboBox;
    }

    private JButton createAnimatedButton(String text, Color normalColor, Color hoverColor, MouseAdapter clickHandler) {
        JButton button = UI.setButton(text, new Font("Arial", Font.BOLD, 12), 
            normalColor, Color.WHITE, 
            BorderFactory.createEmptyBorder(10, 20, 10, 20), 10, null);
        
        button.addMouseListener(new MouseAdapter() {
            private Timer timer;
            private float opacity = 1.0f;
            
            @Override
            public void mouseEntered(MouseEvent e) {
                if (timer != null && timer.isRunning()) {
                    timer.stop();
                }
                
                timer = new Timer(10, event -> {
                    opacity -= 0.05f;
                    if (opacity <= 0) {
                        opacity = 0;
                        timer.stop();
                        button.setBackground(hoverColor);
                    } else {
                        int r = (int)(normalColor.getRed() * opacity + hoverColor.getRed() * (1 - opacity));
                        int g = (int)(normalColor.getGreen() * opacity + hoverColor.getGreen() * (1 - opacity));
                        int b = (int)(normalColor.getBlue() * opacity + hoverColor.getBlue() * (1 - opacity));
                        button.setBackground(new Color(r, g, b));
                    }
                });
                timer.start();
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (timer != null && timer.isRunning()) {
                    timer.stop();
                }
                
                timer = new Timer(10, event -> {
                    opacity += 0.05f;
                    if (opacity >= 1.0f) {
                        opacity = 1.0f;
                        timer.stop();
                        button.setBackground(normalColor);
                    } else {
                        int r = (int)(hoverColor.getRed() * (1 - opacity) + normalColor.getRed() * opacity);
                        int g = (int)(hoverColor.getGreen() * (1 - opacity) + normalColor.getGreen() * opacity);
                        int b = (int)(hoverColor.getBlue() * (1 - opacity) + normalColor.getBlue() * opacity);
                        button.setBackground(new Color(r, g, b));
                    }
                });
                timer.start();
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                Color pressedColor = new Color(
                    Math.max(0, hoverColor.getRed() - 30),
                    Math.max(0, hoverColor.getGreen() - 30),
                    Math.max(0, hoverColor.getBlue() - 30)
                );
                button.setBackground(pressedColor);
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if (button.contains(e.getPoint())) {
                    button.setBackground(hoverColor);
                }
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                if (clickHandler != null) {
                    clickHandler.mouseClicked(e);
                }
            }
        });
        
        return button;
    }

    private JScrollPane createTable() {
        String[] columns = {"User ID", "Full Name", "Email", "Role", "Status"}; 
        Object[][] data = convertUsersToTableData(allUsers);

        tableModel = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        userTable = new JTable(tableModel);
        
        TableUser.styleTable(userTable);
        userTable.setRowHeight(35);
        
        userTable.getColumn("User ID").setCellRenderer(new TableUser.CenteredRenderer());
        userTable.getColumn("Role").setCellRenderer(new TableUser.RoleBadgeRenderer());
        userTable.getColumn("Status").setCellRenderer(new TableUser.StatusBadgeRenderer());
        userTable.getColumn("Email").setCellRenderer(new TableUser.EmailRenderer());
        
        userTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow >= 0) {
                    selectedUser = currentFilteredUsers.get(selectedRow);
                    userDetail.updateUser(selectedUser);
                }
            }
        });

        if (!currentFilteredUsers.isEmpty()) {
            userTable.setRowSelectionInterval(0, 0);
            selectedUser = currentFilteredUsers.get(0);
            userDetail.updateUser(selectedUser);
        }

        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getViewport().setBackground(Color.WHITE);
        return scrollPane;
    }

    private Object[][] convertUsersToTableData(List<User> users) {
        Object[][] data = new Object[users.size()][5];
        
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            data[i][0] = user.getUserId();
            data[i][1] = user.getName() != null && !user.getName().isEmpty() ? user.getName() : user.getUsername();
            data[i][2] = user.getEmail() != null ? user.getEmail() : "N/A";
            data[i][3] = user.getRole() != null ? user.getRole() : "Customer";
            data[i][4] = getUserStatus(user); 
        }
        
        return data;
    }


    private String getUserStatus(User user) {
        if (user.getRole() != null && user.getRole().equalsIgnoreCase("Admin"))  return "Active";
        return "Active"; 
    }

    private void filterUsers(String query) { applyFilters(query); }

    private void applyFilters() { applyFilters(""); }

    private void applyFilters(String searchQuery) {
        List<User> filtered = new ArrayList<>(allUsers);

        if (!currentStatusFilter.equals("All Status"))  filtered.removeIf(user -> !getUserStatus(user).equals(currentStatusFilter));

        if (!currentRoleFilter.equals("All Roles")) {
            filtered.removeIf(user -> {
                String role = user.getRole() != null ? user.getRole() : "Customer";
                return !role.equalsIgnoreCase(currentRoleFilter);
            });
        }

        if (searchQuery != null && !searchQuery.isEmpty() && !searchQuery.equals("Search users...")) {
            String lowerQuery = searchQuery.toLowerCase();
            filtered.removeIf(user -> {
                String name = user.getName() != null ? user.getName().toLowerCase() : "";
                String username = user.getUsername() != null ? user.getUsername().toLowerCase() : "";
                String email = user.getEmail() != null ? user.getEmail().toLowerCase() : "";
                int userId = user.getUserId();
                
                return !name.contains(lowerQuery) && 
                       !username.contains(lowerQuery) && 
                       !email.contains(lowerQuery) && 
                       !(String.valueOf(userId).contains(lowerQuery));
            });
        }

        currentFilteredUsers = filtered;
        refreshTable(currentFilteredUsers);
    }

    private void refreshTable(List<User> users) {
        Object[][] data = convertUsersToTableData(users);
        tableModel.setRowCount(0);  // Clear existing rows
        for (Object[] row : data) { tableModel.addRow(row); }

        if (!users.isEmpty()) {
            userTable.setRowSelectionInterval(0, 0);
            selectedUser = users.get(0);
            userDetail.updateUser(selectedUser);
        } else {
            selectedUser = null;
            userDetail.updateUser(null);
        }
    }

    private void handleAddUser() {
        sidePanel.removeAll();
        sidePanel.add(addUserPanel, BorderLayout.EAST);
        sidePanel.revalidate();
        sidePanel.repaint();
    }

    private void handleUpdateUser() {
        if (selectedUser == null) {
            JOptionPane.showMessageDialog(
                this,
                "Please select a user from the table first.",
                "No User Selected",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        JOptionPane.showMessageDialog(
            this,
            "Update User functionality will be implemented here.\nSelected User: " + selectedUser.getName(),
            "Update User",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void handleDeleteUser() {
        if (selectedUser == null) {
            JOptionPane.showMessageDialog(
                this,
                "Please select a user from the table first.",
                "No User Selected",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Create detailed confirmation message
        String message = String.format("""
                                       Are you sure you want to delete this user?
                                       
                                       User ID: %s
                                       Name: %s
                                       Email: %s
                                       Role: %s
                                       
                                       This action cannot be undone!""",
            selectedUser.getUserId(),
            selectedUser.getName(),
            selectedUser.getEmail(),
            selectedUser.getRole()
        );

        int choice = JOptionPane.showConfirmDialog(
            this,
            message,
            "Confirm Delete User",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            userDAO.delete(selectedUser.getUserId());
            allUsers.removeIf(user -> user.getUserId() == selectedUser.getUserId());
            applyFilters();
        }
    }

    @SuppressWarnings("unused")
    private void refreshUserList() {
        List<User> updatedUsers = userDAO.getAll();
        allUsers.clear();
        allUsers.addAll(updatedUsers);
        applyFilters();
    }
}
