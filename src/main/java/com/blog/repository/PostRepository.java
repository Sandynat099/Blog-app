package com.blog.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.blog.pojo.Post;

public interface PostRepository extends JpaRepository<Post, Integer> {

	@Query("select distinct p from Post p join p.tag t WHERE lower(t.name) like lower(:search ) or lower(p.title) like lower(:search ) or lower(p.user.name) like lower(:search ) or lower(p.content) like lower(:search )")
	public List<Post> searchLists(@Param("search") String search);

	@Query(value = "SELECT distinct post.id,title,excerpt,content,published_at,is_published,post.created_at,post.updated_at FROM post WHERE user_id=?1", nativeQuery = true)
	public List<Post> filterAuthor(int authorId);

	@Query(value = "SELECT distinct post.id,title,excerpt,content,published_at,is_published,post.created_at,post.updated_at FROM post inner JOIN post_tag on post.id = post_tag.post_id left Join tags on tags.id=post_tag.tag_id WHERE tags.id=?1", nativeQuery = true)
	public List<Post> filterTag(int tagId);

	@Query(value = "select * from post where published_at between ?1 and ?2", nativeQuery = true)
	public List<Post> filterByDates(LocalDate fromDate, LocalDate toDate);
	
	@Query(value = "select * from post where is_published=?1", nativeQuery = true)
	public List<Post> findPostByIsPublished(boolean post);
	
	@Query("select distinct p from Post p join p.user u where p.isPublished=:post and u.email=:email ")
	public List<Post> findUserPostByIsPublished(@Param("email")String email,@Param("post")boolean post);
}
