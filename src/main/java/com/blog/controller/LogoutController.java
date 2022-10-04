package com.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutController {
	
	@GetMapping("/logout-success")
	public String showLogoutPage() {
		return "logout.html";

	}
}
