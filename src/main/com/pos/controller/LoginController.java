package main.com.pos.controller;

import main.com.pos.model.User;
import main.com.pos.service.AuthService;

public class LoginController {
	private final AuthService authService;

	public LoginController(AuthService authService) {
		this.authService = authService;
	}

	public User login(User user) {
		return authService.authenticate(user);
	}
}
