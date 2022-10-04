package com.blog.serviceImplementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import com.blog.pojo.User;
import com.blog.repository.UserRepository;
import com.blog.service.UserService;

@Service
public class UserServiceImplement implements UserService{
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public void saveUserData(User user) {
		user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12)));
		user.setRole("USER");
		userRepository.save(user);
	}

	@Override
	public List<User> findAllAuthor() {
		List<User> authorList = userRepository.findAllAuthorByIsPublished();
		return authorList;
	}

	@Override
	public User getByEmail(String user) {
		User userName=userRepository.findByEmail(user);
		return userName;
	}

}
