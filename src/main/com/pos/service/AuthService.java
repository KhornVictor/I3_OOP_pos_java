package main.com.pos.service;
import main.com.pos.dao.UserDAO;
import main.com.pos.model.Users;

public class AuthService {
	public boolean authenticate(String username, String password) {
		Users authorziedUser = new UserDAO().authenticate(username, password);
		if (username == null || password == null) {
			System.out.println("⚠️ Username or password cannot be null");
			return false;
		}
		String trimmedUser = username.trim();
		String trimmedPass = password.trim();
		return authorziedUser != null && authorziedUser.getUsername().equals(trimmedUser) && authorziedUser.getPassword().equals(trimmedPass);
	}
}