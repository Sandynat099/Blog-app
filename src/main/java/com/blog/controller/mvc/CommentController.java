package com.blog.controller.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.blog.service.CommentService;
import com.blog.service.PostService;

@Controller
public class CommentController {

	@Autowired
	private PostService postService;
	@Autowired
	private CommentService commentService;

	@GetMapping("/comment")
	public String newComment(@RequestParam String name, @RequestParam String email, @RequestParam String comment,
			@RequestParam String blogId, Model model) {
		commentService.saveNewComment(name, email, comment, blogId);
		model.addAttribute("postId", blogId);
		return "redirect:/thisBlog?postId=" + blogId;
	}

	@GetMapping("/deleteComment")
	public String deleteComment(@RequestParam String commentId, @RequestParam String blogId) {
		commentService.deleteSelectedComment(commentId);
		return "redirect:/thisBlog?postId=" + blogId;
	}

	@GetMapping("/updateComment")
	public String updateComment(@RequestParam String commentId, Model model, @RequestParam String blogId,
			Authentication authentication) {

		model.addAttribute("Comment", commentService.selectedComment(commentId));
		model.addAttribute("your", postService.selectedPost(blogId));
		model.addAttribute("blogId", blogId);
		if (authentication != null) {
			model.addAttribute("userName", authentication.getName());
			model.addAttribute("admin",
					authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ADMIN")));
		}
		return "thisPost.html";
	}

	@PostMapping("/doUpdateComment")
	public String saveComment(@RequestParam String name, @RequestParam String email, @RequestParam String comment,
			@RequestParam String blogId, @RequestParam String id) {
		commentService.saveUpdatedComment(name, email, comment, blogId, id);
		return "redirect:/thisBlog?postId=" + blogId;
	}

}
