package main.com.pos.view.user;

import java.awt.*;
import javax.swing.*;
import main.com.pos.components.ui.UI;
import main.com.pos.dao.AddressDAO;
import main.com.pos.model.Address;
import main.com.pos.model.User;

public class UserDetailPanel extends JPanel {
    
    private JLabel avatarLabel;
    private JLabel nameLabel;
    private JLabel idLabel;
    private JLabel emailValueLabel;
    private JLabel phoneValueLabel;
    private JLabel usernameValueLabel;
    private JLabel roleTagLabel;
    private JLabel addressValueLabel;
    private final Image defaultAvatar = UI.getImage("images/avatar/victor.png");
    private Image avatarImage = defaultAvatar;
    private User currentUser;

    public UserDetailPanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(350, 700));
        setBackground(new Color(12, 30, 89)); // gray-100    
        add(contentHeader("User Details"), BorderLayout.NORTH);
        add(contentMain(), BorderLayout.CENTER);
    }

    private JPanel contentHeader(String title) {    
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.BLUE); // gray-100
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);

        headerPanel.add(titleLabel, BorderLayout.WEST);

        return headerPanel;
    }

    private JPanel contentMain(){
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        mainPanel.add(imagePanel());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(infoRow());

        return mainPanel;
    }

    private JPanel imagePanel() {
        JPanel imgPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image imageToDraw = avatarImage;
                if (currentUser != null && currentUser.getImage() != null) {
                    imageToDraw = UI.getImage(currentUser.getImage());
                }

                if (imageToDraw != null) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    int w = getWidth();
                    int h = getHeight();
                    g2.drawImage(imageToDraw, 0, 0, w, h, this);
                    g2.dispose();
                } else {
                    g.setColor(Color.WHITE);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };

        imgPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 300));
        imgPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        avatarLabel = new JLabel();
        avatarLabel.setHorizontalAlignment(SwingConstants.CENTER);
        avatarLabel.setPreferredSize(new Dimension(150, 150));
        avatarLabel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

        return imgPanel;
    }

    public void setAvatar(Image image) {
        this.avatarImage = image != null ? image : defaultAvatar;
        Image scaled = avatarImage != null ? avatarImage.getScaledInstance(150, 150, Image.SCALE_SMOOTH) : null;
        avatarLabel.setIcon(scaled != null ? new ImageIcon(scaled) : null);
        repaint();
    }

    public void setAvatar(ImageIcon icon) {
        if (icon != null) setAvatar(icon.getImage());
        else setAvatar((Image) null);
    }

    private JPanel infoRow() {
        JPanel rowPanel = new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.Y_AXIS));
        rowPanel.setBackground(Color.WHITE);
        rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        rowPanel.setBorder(BorderFactory.createEmptyBorder(5,5, 5, 0));

        nameLabel = new JLabel();
        nameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        nameLabel.setForeground(Color.BLACK);
        rowPanel.add(nameLabel);

        rowPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        idLabel = new JLabel();
        idLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        idLabel.setForeground(Color.DARK_GRAY);
        rowPanel.add(idLabel);
        rowPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rowPanel.add(new JSeparator());
        rowPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel usernamLabele = new JLabel("Username:");
        usernamLabele.setFont(new Font("Arial", Font.BOLD, 16));
        usernamLabele.setForeground(Color.BLACK);
        rowPanel.add(usernamLabele);

        usernameValueLabel = new JLabel();
        usernameValueLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        usernameValueLabel.setForeground(Color.DARK_GRAY);
        rowPanel.add(usernameValueLabel);
        rowPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 16));
        emailLabel.setForeground(Color.BLACK);
        rowPanel.add(emailLabel);

        emailValueLabel = new JLabel();
        emailValueLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        emailValueLabel.setForeground(Color.DARK_GRAY);
        rowPanel.add(emailValueLabel);

        rowPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setFont(new Font("Arial", Font.BOLD, 16));
        phoneLabel.setForeground(Color.BLACK);
        rowPanel.add(phoneLabel);

        phoneValueLabel = new JLabel();
        phoneValueLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        phoneValueLabel.setForeground(Color.DARK_GRAY);
        rowPanel.add(phoneValueLabel);
        rowPanel.add(Box.createRigidArea(new Dimension(0, 10)));

       roleTagLabel = new JLabel("Role") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int arc = 100;

                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
                g2.setColor(getForeground().darker()); 
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        roleTagLabel.setFont(new Font("Arial", Font.BOLD, 16));
        roleTagLabel.setForeground(Color.BLACK);
        roleTagLabel.setBackground(Color.RED);
        roleTagLabel.setOpaque(true);
        roleTagLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        rowPanel.add(roleTagLabel);
        rowPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel address = new JPanel();
        address.setLayout(new BoxLayout(address, BoxLayout.Y_AXIS));
        address.setBackground(Color.WHITE);
        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setFont(new Font("Arial", Font.BOLD, 16));
        addressLabel.setForeground(Color.BLACK);
        address.add(addressLabel);
        addressValueLabel = new JLabel("123 Main St, Springfield, USA");
        addressValueLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        addressValueLabel.setForeground(Color.DARK_GRAY);
        address.add(addressValueLabel);
        rowPanel.add(address);

        // initialize with provided user (or placeholder if null)
        updateUser(currentUser);

        return rowPanel;
    }

    public void updateUser(User user) {
        this.currentUser = user;

        String nameText = user != null && user.getName() != null ? user.getName() : "Select a user";
        String idText = user != null ? "ID: " + user.getUserId() : "ID: -";
        String usernameText = user != null && user.getUsername() != null ? user.getUsername() : "-";
        String emailText = user != null && user.getEmail() != null ? user.getEmail() : "-";
        String phoneText = "+1-234-567-8901";
        String roleText = user != null && user.getRole() != null ? user.getRole() : "-";
        String addressText = user != null ? getAddressString(user.getUserId()) : "-";
        
        nameLabel.setText(nameText);
        idLabel.setText(idText);
        usernameValueLabel.setText(usernameText);
        emailValueLabel.setText(emailText);
        phoneValueLabel.setText(phoneText);
        roleTagLabel.setText(roleText);
        addressValueLabel.setText(addressText);
        if (user != null && "admin".equalsIgnoreCase(user.getRole())) {
            roleTagLabel.setBackground(Color.RED);
            roleTagLabel.setForeground(Color.WHITE);
        } else {
            roleTagLabel.setBackground(new Color(100, 200, 150));
            roleTagLabel.setForeground(Color.WHITE);
        }

        Image target = defaultAvatar;
        if (user != null && user.getImage() != null)  target = UI.getImage(user.getImage());
        setAvatar(target);

        revalidate();
        repaint();
    }

    private String getAddressString(int userId) {
        Address address = new AddressDAO().getById(userId);
        if (address != null)  return String.format("<html>Street: %s<br>State: %s<br>City: %s<br>ZipCode: %s<br>Country: %s</html>", address.getStreet(), address.getState(), address.getCity(), address.getZipCode(),address.getCountry());
        return "-";
    }
}