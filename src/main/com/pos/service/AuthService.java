package main.com.pos.service;
import main.com.pos.dao.UserDAO;
import main.com.pos.model.User;
import main.com.pos.util.Color;

public class AuthService {
	public User authenticate(User user) {
		User authorizedUser = new UserDAO().authenticate(user);
		if (user.getUsername() == null || user.getPassword() == null) {
			System.out.println("⚠️ Username or password cannot be null");
			return null;
		}
		String trimmedUser = user.getUsername().trim();
		String trimmedPass = user.getPassword().trim();
		if (authorizedUser != null && authorizedUser.getUsername().equals(trimmedUser) && authorizedUser.getPassword().equals(trimmedPass)) {
			System.out.println(Color.GREEN + "🟢  Authentication successful for user: " + trimmedUser + Color.RESET);
			return authorizedUser;
		} else {
			System.out.println("❌ Authentication failed for user: " + trimmedUser);
			return null;
		}
	}
}