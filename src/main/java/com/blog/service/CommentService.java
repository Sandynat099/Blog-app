package com.blog.service;

import com.blog.pojo.Comment;

public interface CommentService {
	
	public void saveUpdatedComment(String name,String email, String comment,String blogId, String id);
	
	public Comment selectedComment(String id);
	
	public void deleteSelectedComment(String commentId);
	
	public void saveNewComment(String name, String email,String comment,String blogId);
}
