package main.com.pos.service;

public class AuthService {
	private static final String DEFAULT_USERNAME = "admin";
	private static final String DEFAULT_PASSWORD = "password123";
	

	public boolean authenticate(String username, String password) {
		if (username == null || password == null) {
			System.out.println("⚠️ Username or password cannot be null");
			return false;
		}
		String trimmedUser = username.trim();
		return DEFAULT_USERNAME.equalsIgnoreCase(trimmedUser) && DEFAULT_PASSWORD.equals(password);
	}
}
