package com.solvd.micro9.vkontaktah.service;

import com.solvd.micro9.vkontaktah.domain.Like;
import com.solvd.micro9.vkontaktah.domain.Post;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {

    List<Post> getAll(Pageable pageable);

    List<Post> findByAuthor(String authorId, String cursor, int pageSize);

    List<Post> findByLiker(String likerId, String cursor, int pageSize);

    List<Post> findAuthorTop(String authorId, Integer count);

    Post save(Post post);

    Post like(String postId, Like like);

}
