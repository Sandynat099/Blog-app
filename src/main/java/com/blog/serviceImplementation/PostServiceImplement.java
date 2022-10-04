package com.blog.serviceImplementation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.blog.pojo.Post;
import com.blog.pojo.Tag;
import com.blog.pojo.User;
import com.blog.repository.PostRepository;
import com.blog.repository.TagRepository;
import com.blog.repository.UserRepository;
import com.blog.service.PostService;

@Service
public class PostServiceImplement implements PostService {

	@Autowired
	private TagRepository tagRepository;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public void saveNewPublishedPost(String tags, Post newPost, Authentication authentication) {

		User user = userRepository.findByEmail(authentication.getName());

		String[] tagNames = tags.split(",");

		List<Tag> tagList = new ArrayList<>();
		List<Tag> existedTagLists = new ArrayList<>();

		for (int i = 0; i < tagNames.length; i++) {

			Tag tag = new Tag();

			Tag thisTag = tagRepository.findByName(tagNames[i]);

			if (thisTag != null) {

				existedTagLists.add(thisTag);
				newPost.setTag(existedTagLists);
				newPost.setPublishedAt(LocalDateTime.now());
				newPost.setCreatedAt(LocalDateTime.now());
				if (newPost.getContent().length() >= 50) {
					newPost.setExcerpt(newPost.getContent().substring(0, 49));
				} else {
					newPost.setExcerpt(newPost.getContent());
				}

				newPost.setUser(user);
				postRepository.save(newPost);
			} else {
				tag.setName(tagNames[i]);
				tag.setCreated_at(LocalDateTime.now());
				tagRepository.save(tag);

				tagList.add(tagRepository.findByName(tagNames[i]));

				newPost.setTag(tagList);
				newPost.setTag(existedTagLists);
				newPost.setPublishedAt(LocalDateTime.now());
				newPost.setCreatedAt(LocalDateTime.now());
				if (newPost.getContent().length() >= 50) {
					newPost.setExcerpt(newPost.getContent().substring(0, 49));
				} else {
					newPost.setExcerpt(newPost.getContent());
				}
				newPost.setUser(user);
				postRepository.save(newPost);
			}

		}

	}

	@Override
	public Post selectedPost(String postId) {
		int id = Integer.parseInt(postId);
		Optional<Post> yourBlog = postRepository.findById(id);
		return yourBlog.get();
	}

	@Override
	public void saveUpdatedPost(String tags, Post update) {
		String[] tagNames = tags.split(",");

		List<Tag> tagLists = new ArrayList<>();
		List<Tag> existedTagLists = new ArrayList<>();

		for (int i = 0; i < tagNames.length; i++) {

			Tag tag = new Tag();

			Tag thisTag = tagRepository.findByName(tagNames[i]);

			if (thisTag != null) {
				existedTagLists.add(thisTag);
				update.setTag(existedTagLists);
				update.setUpdatedAt(LocalDateTime.now());
				if (update.getContent().length() >= 50) {
					update.setExcerpt(update.getContent().substring(0, 49));
				} else {
					update.setExcerpt(update.getContent());
				}
				postRepository.save(update);
			} else {
				tag.setName(tagNames[i]);
				tag.setUpdated_at(LocalDateTime.now());
				tagRepository.save(tag);

				tagLists.add(tagRepository.findByName(tagNames[i]));
				update.setTag(tagLists);
				update.setUpdatedAt(LocalDateTime.now());
				if (update.getContent().length() >= 50) {
					update.setExcerpt(update.getContent().substring(0, 49));
				} else {
					update.setExcerpt(update.getContent());
				}
				postRepository.save(update);
			}
		}

	}

	@Override
	public Post updatePost(String name) {
		int id = Integer.parseInt(name);
		Optional<Post> newUpdates = postRepository.findById(id);
		return newUpdates.get();
	}

	@Override
	public void deleteThisPost(String postId) {
		int id = Integer.parseInt(postId);
		postRepository.deleteById(id);

	}

	@Override
	public List<Post> sortByDesc(List<Post> list) {
		list.sort(Comparator.comparing(Post::getPublishedAt).reversed());
		return list;
	}

	@Override
	public List<Post> sortByAsc(List<Post> list) {
		list.sort(Comparator.comparing(Post::getPublishedAt));
		return list;
	}

	@Override
	public List<Post> filterByDate(String fromDate, String toDate) {
		LocalDate toLocalDate = LocalDate.parse(toDate);
		LocalDate fromLocalDate = LocalDate.parse(fromDate);
		List<Post> posts = postRepository.filterByDates(fromLocalDate, toLocalDate);

		return posts;
	}

	@Override
	public List<Post> IntersectionOfBoth(List<Post> InList, List<Post> ofList) {
		List<Post> list = InList.stream().filter(ofList::contains).collect(Collectors.toList());
		return list;
	}

	@Override
	public Page<Post> getPagination(List<Post> list, Pageable pageable) {
		int start = Math.min((int) pageable.getOffset(), list.size());
		int end = Math.min((start + pageable.getPageSize()), list.size());
		Page<Post> pages = new PageImpl<>(list.subList(start, end), pageable, list.size());
		return pages;
	}

	@Override
	public List<Post> filterByAuthor(String author) {
		String[] authorsList = author.split(",");
		List<Post> posts = new ArrayList<>();
		for (int i = 0; i < authorsList.length; i++) {

			List<Post> post = postRepository.filterAuthor(Integer.parseInt(authorsList[i]));
			posts.addAll(post);
		}
		return posts;
	}

	@Override
	public List<Post> filterByTag(String tag) {

		String[] tagsList = tag.split(",");
		List<Post> posts = new ArrayList<>();
		for (int j = 0; j < tagsList.length; j++) {
			List<Post> post = postRepository.filterTag(Integer.parseInt(tagsList[j]));

			if (!posts.containsAll(post)) {

				posts.addAll(post);
			}

		}
		return posts;
	}

	@Override
	public List<Post> searchedPosts(String search) {
		List<Post> posts = postRepository.searchLists("%" + search + "%");
		return posts;
	}

	@Override
	public List<Post> getAllPosts(boolean post) {
		List<Post> posts = postRepository.findPostByIsPublished(post);
		return posts;
	}

	@Override
	public void publishDraftPost(String draftId) {
		Optional<Post> post = postRepository.findById(Integer.parseInt(draftId));
		Post draftPost = post.get();
		draftPost.setIsPublished(true);
		postRepository.save(draftPost);

	}

	@Override
	public List<Post> getUserDraftPosts(String userName, boolean post) {
		List<Post> posts = postRepository.findUserPostByIsPublished(userName, post);
		return posts;
	}
}
