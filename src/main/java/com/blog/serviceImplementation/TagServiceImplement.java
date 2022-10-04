package com.blog.serviceImplementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.blog.pojo.Post;
import com.blog.pojo.Tag;
import com.blog.repository.PostRepository;
import com.blog.repository.TagRepository;
import com.blog.service.TagService;


@Service
public class TagServiceImplement implements TagService{
	
	
	
	@Autowired
	private TagRepository tagRepository;
	@Autowired
	private PostRepository postRepository;

	@Override
	public String updateTag(String name) {
		
		int id = Integer.parseInt(name);
		Optional<Post> newUpdates = postRepository.findById(id);
		List<String> tags = new ArrayList<>();
		for (Tag tag : newUpdates.get().getTag()) {
			tags.add(tag.getName());
		}
		String updatedTag = String.join(",", tags);
		return updatedTag;
	}

	@Override
	public List<Tag> getAllTagNames() {
		List<Tag> tagNames = tagRepository.findAll();
		System.out.println(tagNames);
		return tagNames;
	}

}
