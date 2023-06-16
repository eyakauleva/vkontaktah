package com.solvd.micro9.vkontaktah.service;

import com.solvd.micro9.vkontaktah.domain.Like;
import com.solvd.micro9.vkontaktah.domain.Post;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface PostService {

    List<Post> getAll(Pageable pageable);

    List<Post> findByAuthor(String authorId, Pageable pageable);

    List<Post> findByEstimator(String estimatorId, Pageable pageable);

    Post findById(String postId);

    Post save(Post post);

    Post like(String postId, Like like);

}
