package com.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.pojo.User;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
	public User findByEmail(String email);

	@Query("select distinct u from User u join u.post p where p.isPublished is true")
    public List<User> findAllAuthorByIsPublished();

}
