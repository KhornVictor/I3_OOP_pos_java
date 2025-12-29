package main.com.pos.model;

public class User {
    private int userId;
    private String username;
    private String password;
    private String role;
    private String name;
    private String email;

    public User() {}


    public User(String name) {
        setName(name);
    }

    public User(String username, String password) {
        setUsername(username);
        setPassword(password);
    }

    public User(int userId, String username, String password, String role, String name, String email) {
        this.userId = userId;
        setUsername(username);
        setPassword(password);
        setRole(role);
        setName(name);
        setEmail(email);
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public final void setUsername(String username) { 
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        this.username = username; 
    }

    public String getPassword() { return password; }
    public final void setPassword(String password) { 
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }
        this.password = password; 
    }

    public String getRole() { return role; }
    public final void setRole(String role) { 
        if (role != null && !role.isEmpty()) {
            this.role = role; 
        }
    }

    public String getName() { return name; }
    public final void setName(String name) { 
        // Allow nullable/empty names to match DB schema and avoid blocking login
        this.name = (name == null) ? "" : name.trim(); 
    }

    public String getEmail() { return email; }
    public final void setEmail(String email) { 
        if (email != null && !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = email; 
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

