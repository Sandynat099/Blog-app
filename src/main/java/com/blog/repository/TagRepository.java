package com.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.pojo.Tag;


public interface TagRepository extends JpaRepository<Tag, Integer> {

	Tag findByName(String name);

}
