package com.blog.serviceImplementation;


import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.blog.pojo.Comment;
import com.blog.pojo.Post;
import com.blog.repository.CommentRepository;
import com.blog.repository.PostRepository;
import com.blog.service.CommentService;

@Service
public class CommentServiceImplement implements CommentService {

	   @Autowired
	    private CommentRepository commentRepository ;
	   
	   @Autowired
		private PostRepository postRepository;

		@Override
		public void saveUpdatedComment(String name,String email, String comment,String blogId, String id) {
			int commentId = Integer.parseInt(id);
			int postId = Integer.parseInt(blogId);
			Optional<Post> posts = postRepository.findById(postId);
			Comment update = new Comment();
			update.setId(commentId);
			update.setName(name);
			update.setEmail(email);
			update.setComment(comment);
			update.setPost(posts.get());
			update.setUpdated_at(LocalDateTime.now());
			commentRepository.save(update);
			
		}


		@Override
		public Comment selectedComment(String commentId) {
			int id = Integer.parseInt(commentId);
			Optional<Comment> updateThisComment = commentRepository.findById(id);
			return updateThisComment.get();
		}


		@Override
		public void deleteSelectedComment(String commentId) {
			int id = Integer.parseInt(commentId);
			commentRepository.deleteById(id);
			
		}


		@Override
		public void saveNewComment(String name, String email,String comment,String blogId) {
			Comment com = new Comment();
			int id = Integer.parseInt(blogId);
			Optional<Post> posts = postRepository.findById(id);
			com.setComment(comment);
			com.setName(name);
			com.setEmail(email);
			com.setPost(posts.get());
			com.setCreated_at(LocalDateTime.now());
			commentRepository.save(com);
			
		}

		
}
