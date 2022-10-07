package com.blog.controller.restApi;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blog.pojo.Post;
import com.blog.service.PostService;

@RestController
public class HomeRestController {

	@Autowired
	private PostService postService;
	
	@GetMapping("/api")
	public List<Post> getAllBlog(
			@RequestParam(value = "pageNo", defaultValue = "1", required = false) Integer pageNo,
			@RequestParam(required = false) String search, @RequestParam(required = false) String authorId,
			@RequestParam(required = false) String tag, @RequestParam(required = false) String sortBy,
			@RequestParam(required = false) String fromDate, @RequestParam(required = false) String toDate) {
	
	
		List<Post> posts = postService.getAllPosts(true);
	
		if (search != null) {
			posts = postService.IntersectionOfBoth(posts, postService.searchedPosts(search));
			
		}
	
		if (authorId != null) {
			posts = postService.IntersectionOfBoth(posts, postService.filterByAuthor(authorId));
			
		}
		if (tag != null) {
			posts = postService.IntersectionOfBoth(posts, postService.filterByTag(tag));
			
		}
		if (fromDate != null && toDate != null && !fromDate.equals("")) {
			posts = postService.IntersectionOfBoth(posts, postService.filterByDate(fromDate, toDate));
			
		}
	
		if (sortBy != null) {
			if (sortBy.equals("desc")) {
	
				posts = postService.sortByDesc(posts);
			
	
			}
			if (sortBy.equals("asc")) {
	
				posts=postService.sortByAsc(posts);
				
			}
	}
		int pageSize = 4;
	Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		Page<Post> pages = postService.getPagination(posts, pageable);
		List<Post> filteredPosts = pages.getContent();
	
		 return filteredPosts;
	}

}
