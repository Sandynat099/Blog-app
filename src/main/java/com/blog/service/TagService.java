package com.blog.service; 

import java.util.List;

import com.blog.pojo.Tag;

public interface TagService {
public String updateTag(String name);
public List<Tag> getAllTagNames();
}
