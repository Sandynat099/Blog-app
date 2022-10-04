package com.blog.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.blog.pojo.Post;
import com.blog.service.PostService;
import com.blog.service.TagService;
import com.blog.service.UserService;

@Controller
public class HomeController {

	@Autowired
	private PostService postService;
	@Autowired
	private TagService tagService;
	@Autowired
	private UserService userService;

	@GetMapping("/")
	public String goToHome() {
		return "redirect:/blog";
	}

	@GetMapping("/blog")
	public String printAllBlog(Model model,
			@RequestParam(value = "pageNo", defaultValue = "1", required = false) Integer pageNo,
			@RequestParam(required = false) String search, @RequestParam(required = false) String authorId,
			@RequestParam(required = false) String tag, @RequestParam(required = false) String sortBy,
			@RequestParam(required = false) String fromDate, @RequestParam(required = false) String toDate) {

		model.addAttribute("authors", userService.findAllAuthor());
		model.addAttribute("tagNames", tagService.getAllTagNames());

		int pageSize = 4;
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		model.addAttribute("currentPage", pageNo);

		List<Post> posts = postService.getAllPosts(true);

		if (search != null) {
			posts = postService.IntersectionOfBoth(posts, postService.searchedPosts(search));
			model.addAttribute("search", search);
		}

		if (authorId != null) {
			posts = postService.IntersectionOfBoth(posts, postService.filterByAuthor(authorId));
			model.addAttribute("author", authorId);
		}
		if (tag != null) {
			posts = postService.IntersectionOfBoth(posts, postService.filterByTag(tag));
			model.addAttribute("tag", tag);
		}
		if (fromDate != null && toDate != null && !fromDate.equals("")) {
			posts = postService.IntersectionOfBoth(posts, postService.filterByDate(fromDate, toDate));
			model.addAttribute("fromDate", fromDate);
			model.addAttribute("toDate", toDate);
		}

		if (sortBy != null) {
			if (sortBy.equals("desc")) {

				posts = postService.sortByDesc(posts);
				model.addAttribute("sortBy", sortBy);

			}
			if (sortBy.equals("asc")) {

				postService.sortByAsc(posts);
				model.addAttribute("sortBy", sortBy);
			}
		}
		Page<Post> pages = postService.getPagination(posts, pageable);
		List<Post> filteredPosts = pages.getContent();
		model.addAttribute("totalPages", pages.getTotalPages());
		model.addAttribute("posts", filteredPosts);

		return "blog.html";
	}

}
