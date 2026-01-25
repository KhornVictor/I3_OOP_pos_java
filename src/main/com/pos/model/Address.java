package main.com.pos.model;

public class Address {
    private int addressId;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;

    public Address() {}

    public Address(int addressId, String street, String city, String state, String zipCode, String country) {
        this.addressId = addressId;
        setStreet(street);
        setCity(city);
        setState(state);
        setZipCode(zipCode);
        setCountry(country);
    }
    
    public int getAddressId() { return addressId; }
    public String getStreet() { return street; }
    public String getCity() { return city; }
    public String getState() { return state; }  
    public String getZipCode() { return zipCode; }
    public String getCountry() { return country; }

    public void setAddressId(int addressId) { this.addressId = addressId; }
    public final void setStreet(String street) { 
        if (street == null || street.trim().isEmpty()) throw new IllegalArgumentException("Street cannot be null or empty");
        this.street = street; 
    }
    public final void setCity(String city) { 
        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("City cannot be null or empty");
        }
        this.city = city; 
    }
    public final void setState(String state) { 
        if (state == null || state.trim().isEmpty()) {
            throw new IllegalArgumentException("State cannot be null or empty");
        }
        this.state = state; 
    }

    public final void setZipCode(String zipCode) { 
        if (zipCode == null || zipCode.trim().isEmpty()) {
            throw new IllegalArgumentException("ZipCode cannot be null or empty");
        }
        this.zipCode = zipCode; 
    }

    public final void setCountry(String country) { 
        if (country == null || country.trim().isEmpty()) {
            throw new IllegalArgumentException("Country cannot be null or empty");
        }
        this.country = country; 
    }

    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zipCode='" + zipCode + '\'' +
                '}';
    }
}
