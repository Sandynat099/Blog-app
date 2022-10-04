package com.blog.service;

import java.util.List;

import com.blog.pojo.User;


public interface UserService {

	public void saveUserData(User user);
	
	List<User> findAllAuthor();

	User getByEmail(String user);
}
