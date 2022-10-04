package com.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.blog.pojo.User;
import com.blog.service.UserService;




@Controller
public class LoginController {

	@Autowired
	private UserService userService;

	@GetMapping("/login")
	public String showLoginPage() {
		return "login.html";

	}

	@PostMapping("/login")
	public String login() {

		return "redirect:/";
	}

	@GetMapping("/createAccount")
	public String showSignUpPage() {
		return "createAccount.html";

	}

	@GetMapping("/loginForm")
	public String saveUserDate(@ModelAttribute User user, Authentication authentication, Model model) {
		User userName = userService.getByEmail(user.getEmail());
		if (userName != null) {
			model.addAttribute("present", "This email already exist");
			return "createAccount.html";
		} else {
			userService.saveUserData(user);
			return "redirect:/blog";
		}
	}

}
