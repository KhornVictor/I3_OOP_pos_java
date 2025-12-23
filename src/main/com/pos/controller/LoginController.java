package main.com.pos.controller;

import main.com.pos.service.AuthService;

public class LoginController {
	private final AuthService authService;

	public LoginController(AuthService authService) {
		this.authService = authService;
	}

	public boolean login(String username, String password) {
		return authService.authenticate(username, password);
	}
}
