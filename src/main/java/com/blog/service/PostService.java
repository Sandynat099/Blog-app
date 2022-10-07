package com.blog.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import com.blog.pojo.Post;

public interface PostService {
	
public void saveNewPublishedPost(String tags,Post newPost,Authentication authentication);

public Post selectedPost(String postId);

public void saveUpdatedPost(String tags,Post update,String postId);

public Post updatePost(String name);

public void deleteThisPost(String postId);


public List<Post> sortByDesc(List<Post> list);
public List<Post> sortByAsc(List<Post> list);

public List<Post> filterByDate(String fromDate,String toDate);


public List<Post> IntersectionOfBoth(List<Post> InList,List<Post> ofList);

public Page<Post> getPagination(List<Post> list,Pageable pageable);

public List<Post> filterByAuthor(String author);

public List<Post> filterByTag(String tagId);

public List<Post> searchedPosts(String search);

public List<Post> getAllPosts(boolean post);

public void publishDraftPost(String draftId);

public List<Post> getUserDraftPosts(String userName,boolean post);

public Post getPostById(int postId);


}