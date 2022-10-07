package com.blog.controller.restApi;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blog.pojo.Post;
import com.blog.service.PostService;

@RestController
public class PostRestController {
	
	@Autowired
	private PostService postService;

	
	@PostMapping("/api/post")
	public ResponseEntity<Post> publish(@RequestBody Post newPost, @RequestParam String tags, Authentication authentication) {
	  postService.saveNewPublishedPost(tags, newPost, authentication);
		return ResponseEntity.status(HttpStatus.CREATED).build();

	}
	
	
	@GetMapping("/api/{postId}")
	public Post showSelectedBlog(@PathVariable String postId) {
		return	postService.selectedPost(postId);
		
	}

	@DeleteMapping("/api/delete/{postId}")
	public ResponseEntity<String> deletePost(@PathVariable String postId, Authentication authentication) {
		if(postService.selectedPost(postId).getUser().getEmail().equals(authentication.getName()) || authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
		postService.deleteThisPost(postId);
		return ResponseEntity.status(HttpStatus.OK).build();
		} else {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized");
		}
	}
	
	@PutMapping("/api/update/{postId}")
	public ResponseEntity<String> updatePost(@RequestBody Post updatePost,@PathVariable String postId,@RequestParam String tags,Authentication authentication) {
		
		if(postService.selectedPost(postId).getUser().getEmail().equals(authentication.getName()) || authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
			postService.saveUpdatedPost(tags, updatePost, postId);
			return ResponseEntity.status(HttpStatus.OK).build();
			} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized");
			}
	}
	@GetMapping("/api/draft")
	public List<Post> showDrafts(Authentication authentication,@RequestParam(value = "pageNo", defaultValue = "1", required = false) Integer pageNo) {
		int pageSize = 4;
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		List<Post> posts = new ArrayList<>();
		if (authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ADMIN"))) {
			posts = postService.getAllPosts(false);
		} else {
			posts = postService.getUserDraftPosts(authentication.getName(), false);
		}
		Page<Post> pages = postService.getPagination(posts, pageable);
		List<Post> filteredPosts = pages.getContent();
		return filteredPosts;
	}
}
