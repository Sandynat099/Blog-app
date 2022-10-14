package com.blog.controller.mvc;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.blog.pojo.Post;
import com.blog.pojo.User;
import com.blog.service.PostService;
import com.blog.service.TagService;
import com.blog.service.UserService;

@Controller
public class PostControlller {
	@Autowired
	private PostService postService;
	@Autowired
	private TagService tagService;
	@Autowired
	private UserService userService;

	@GetMapping("/post")
	public String showForm() {
		return "postPage.html";
	}

	@PostMapping("/publish")
	public String publish(@ModelAttribute Post newPost, @RequestParam String tags, Authentication authentication) {
		postService.saveNewPublishedPost(tags, newPost, authentication);
		return "redirect:/blog";

	}

	@GetMapping("/draft")
	public String showDrafts(Authentication authentication, Model model,
			@RequestParam(value = "pageNo", defaultValue = "1", required = false) Integer pageNo) {
		int pageSize = 4;
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		model.addAttribute("currentPage", pageNo);
		List<Post> posts = new ArrayList<>();
		if (authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ADMIN"))) {
			posts = postService.getAllPosts(false);
		} else {
			posts = postService.getUserDraftPosts(authentication.getName(), false);
		}
		Page<Post> pages = postService.getPagination(posts, pageable);
		List<Post> filteredPosts = pages.getContent();
		model.addAttribute("totalPages", pages.getTotalPages());
		model.addAttribute("posts", filteredPosts);
		model.addAttribute("currentPage", pageNo);
		return "draft.html";
	}

	@GetMapping("/thisBlog")
	public String showSelectedBlog(@RequestParam(required = false) String postId, Model model,
			Authentication authentication, @RequestParam(required = false) String draftId) {
		if (draftId != null) {
			postService.publishDraftPost(draftId);
			return "redirect:/blog";
		} else {
			model.addAttribute("your", postService.selectedPost(postId));
			if (authentication != null) {
				model.addAttribute("userName", authentication.getName());
				User user = userService.getByEmail(authentication.getName());
				model.addAttribute("authorName", user.getName());
				model.addAttribute("admin",
						authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ADMIN")));
			}
			return "thisPost.html";
		}
	}

	@GetMapping("/delete")
	public String deletePost(@RequestParam String name) {
		postService.deleteThisPost(name);
		return "redirect:/blog";
	}

	@GetMapping("/doUpdate")
	public String updatePost(@RequestParam String name, Model model) {
		model.addAttribute("Update", postService.updatePost(name));
		model.addAttribute("tags", tagService.updateTag(name));
		return "update.html";
	}

	@PostMapping("/update")
	public String savePost(@ModelAttribute Post update, @RequestParam String tags, @RequestParam String id) {
		postService.saveUpdatedPost(tags, update,id);
		return "redirect:/blog";
	}

}
