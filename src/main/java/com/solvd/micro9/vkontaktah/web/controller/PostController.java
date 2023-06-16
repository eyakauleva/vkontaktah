package com.solvd.micro9.vkontaktah.web.controller;

import com.solvd.micro9.vkontaktah.domain.Like;
import com.solvd.micro9.vkontaktah.domain.Post;
import com.solvd.micro9.vkontaktah.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @QueryMapping("getAllPosts")
    public List<Post> getAll(@Argument final int size,
                             @Argument final int page) {
        Pageable pageable = PageRequest.of(page, size);
        return postService.getAll(pageable);
    }

    @MutationMapping("savePost")
    public Post save(@Argument final Post post) {
        return postService.save(post);
    }

    @MutationMapping("likePost")
    public Post like(@Argument final String postId,
                     @Argument final Like like) {
        return postService.like(postId, like);
    }

}
