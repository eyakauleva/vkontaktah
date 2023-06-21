package com.solvd.micro9.vkontaktah.service.impl;

import com.solvd.micro9.vkontaktah.domain.Like;
import com.solvd.micro9.vkontaktah.domain.Post;
import com.solvd.micro9.vkontaktah.domain.exception.ResourceNotFoundException;
import com.solvd.micro9.vkontaktah.persistence.PostRepository;
import com.solvd.micro9.vkontaktah.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public List<Post> getAll(final Pageable pageable) {
        return postRepository.findAll(pageable)
                .toList();
    }

    @Override
    public List<Post> findByAuthor(final String authorId, final Pageable pageable) {
        return postRepository.findByAuthor(
                authorId,
                pageable.getOffset(),
                pageable.getPageSize()
        );
    }

    @Override
    public List<Post> findByLiker(final String likerId, final Pageable pageable) {
        return postRepository.findByLiker(
                likerId,
                pageable.getOffset(),
                pageable.getPageSize()
        );
    }

    @Override
    public List<Post> findAuthorTop(final String authorId, final Integer count) {
        return postRepository.findAuthorTop(authorId, count);
    }

    @Override
    public Post save(final Post post) {
        String authorId = post.getAuthor().getId();
        post.setId(UUID.randomUUID().toString());
        post.setIsEdited(false);
        post.setAuthor(null);
        Post savedPost = postRepository.save(post);
        return postRepository.setAuthor(authorId, savedPost.getId());
    }

    @Override
    public Post like(final String postId, final Like like) {
        try {
            return postRepository.addLike(like.getUser().getId(), postId, like.getValue());
        } catch (NoSuchElementException ex) {
            throw new ResourceNotFoundException(
                    "Post or user does not exist. Check the provided ids"
            );
        }
    }

}
