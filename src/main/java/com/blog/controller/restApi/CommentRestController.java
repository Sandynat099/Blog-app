package com.blog.controller.restApi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.blog.pojo.Comment;
import com.blog.service.CommentService;
import com.blog.service.PostService;

@RestController
public class CommentRestController {
	
	@Autowired
	private PostService postService;
	@Autowired
	private CommentService commentService;
	
	@PostMapping("/api/{postId}/comment")
	public ResponseEntity<Comment> saveNewComment(@RequestBody Comment comment,@PathVariable String postId) {
		commentService.saveNewComment(comment.getName(), comment.getEmail(), comment.getComment(), postId);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@DeleteMapping("/api/comment/{commentId}")
	public ResponseEntity<String> deleteComment(@PathVariable String commentId,Authentication authentication) {
		
		if(commentService.selectedComment(commentId).getPost().getUser().getEmail().equals(authentication.getName()) || authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
			commentService.deleteSelectedComment(commentId);
			return ResponseEntity.status(HttpStatus.OK).build();
			} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized");
			}
	}
	
	@PutMapping("/api/update/{postId}/comment/{commentId}")
	public ResponseEntity<String> saveComment(@RequestBody Comment comment,@PathVariable String postId,@PathVariable String commentId,Authentication authentication) {
		if(postService.selectedPost(postId).getUser().getEmail().equals(authentication.getName()) || authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
			commentService.saveUpdatedComment(comment.getName(), comment.getEmail(), comment.getComment(), postId, commentId);
			return ResponseEntity.status(HttpStatus.OK).build();
			} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized");
			}
		
	}
}
