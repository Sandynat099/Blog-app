package com.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.blog.pojo.User;
import com.blog.repository.UserRepository;
import com.blog.serviceImplementation.UserDetailsImplement;

@Service
public class MyUserDetailsService implements UserDetailsService {
	
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	
		User user=userRepository.findByEmail(username);
		if(user==null) {
			throw new UsernameNotFoundException("User 404");
		}
		return new UserDetailsImplement(user);
	}

}
