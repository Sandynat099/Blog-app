package com.blog.controller.restApi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.blog.pojo.User;
import com.blog.service.UserService;

@RestController
public class LoginRestController {

	@Autowired
	private UserService userService;
	
	@PostMapping("/api/signUp")
	public ResponseEntity<String> saveUserData(@RequestBody User user) {
		User userName = userService.getByEmail(user.getEmail());
		if (userName != null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("This Email is Already Exist");
		} else {
			userService.saveUserData(user);
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}
	}
}
