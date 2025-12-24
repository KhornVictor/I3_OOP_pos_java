package main.com.pos.model;

public class Customer {
    private int customerId;
    private String name;
    private String email;
    private String phone;
    private Address address;

    public Customer() {}
    public Customer(int customerId, String name, String email, String phone, Address address) {
        this.customerId = customerId;
        setName(name);
        setEmail(email);
        setPhone(phone);
        this.address = address;
    }

    public int getCustomerId() { return customerId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public Address getAddress() { return address; }

    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public final void setName(String name) { 
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be null or empty");
        }
        this.name = name; 
    }
    public final void setEmail(String email) { 
        if (email != null && !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = email; 
    }
    public final void setPhone(String phone) { this.phone = phone; }
    public void setAddress(Address address) { this.address = address; }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}