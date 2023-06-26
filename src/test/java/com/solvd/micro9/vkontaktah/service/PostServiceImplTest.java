package com.solvd.micro9.vkontaktah.service;

import com.solvd.micro9.vkontaktah.domain.Like;
import com.solvd.micro9.vkontaktah.domain.Post;
import com.solvd.micro9.vkontaktah.domain.User;
import com.solvd.micro9.vkontaktah.domain.exception.ResourceNotFoundException;
import com.solvd.micro9.vkontaktah.persistence.PostRepository;
import com.solvd.micro9.vkontaktah.service.impl.PostServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@ExtendWith(MockitoExtension.class)
public class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostServiceImpl postService;

    @ParameterizedTest
    @MethodSource("com.solvd.micro9.vkontaktah.service.TestDataProvider#getPosts")
    void verifyAllPostsAreReceived(final List<Post> expectedPosts) {
        Pageable pageable = PageRequest.of(0, 100);
        Mockito.when(postRepository.findAll(Mockito.any(Pageable.class)))
                .thenReturn(
                        new PageImpl<>(
                                expectedPosts,
                                pageable,
                                expectedPosts.size()
                        )
                );
        List<Post> resultPosts = postService.getAll(pageable);
        Assertions.assertNotNull(resultPosts);
        Assertions.assertEquals(expectedPosts, resultPosts);
    }

    @ParameterizedTest
    @MethodSource(
            "com.solvd.micro9.vkontaktah.service.TestDataProvider#getPostsWithAuthorId"
    )
    void verifyPostsAreFoundByAuthor(
            final List<Post> expectedPosts, final String authorId
    ) {
        String cursor = "0";
        int pageSize = 100;
        Mockito.when(
                        postRepository.findByAuthor(
                                authorId, cursor, pageSize
                        )
                )
                .thenReturn(expectedPosts);
        List<Post> resultPosts = postService.findByAuthor(authorId, cursor, pageSize);
        Assertions.assertNotNull(resultPosts);
        Assertions.assertEquals(expectedPosts, resultPosts);
        resultPosts.forEach(
                post -> Assertions.assertEquals(authorId, post.getAuthor().getId())
        );
    }

    @ParameterizedTest
    @MethodSource(
            "com.solvd.micro9.vkontaktah.service.TestDataProvider#getPostsWithLikerId"
    )
    void verifyPostsAreFoundByLiker(final List<Post> expectedPosts, final String likerId) {
        String cursor = "0";
        int pageSize = 100;
        Mockito.when(
                        postRepository.findByLiker(
                                likerId, cursor, pageSize
                        )
                )
                .thenReturn(expectedPosts);
        List<Post> resultPosts = postService.findByLiker(likerId, cursor, pageSize);
        Assertions.assertNotNull(resultPosts);
        Assertions.assertEquals(expectedPosts, resultPosts);
        resultPosts.forEach(post -> {
            boolean isLikeSet = false;
            for (Like like : post.getLikes()) {
                if (likerId.equals(like.getUser().getId())) {
                    isLikeSet = true;
                    break;
                }
            }
            Assertions.assertTrue(isLikeSet);
        });
    }

    @ParameterizedTest
    @MethodSource(
            "com.solvd.micro9.vkontaktah.service.TestDataProvider#getSortedByAvgValuePostsWithAuthorId"
    )
    void verifyTopAuthorsPostsAreFound(
            final List<Post> expectedPosts, final String authorId
    ) {
        int count = 5;
        Mockito.when(
                        postRepository.findAuthorTop(authorId, count)
                )
                .thenReturn(expectedPosts);
        List<Post> resultPosts = postService.findAuthorTop(authorId, count);
        Assertions.assertNotNull(resultPosts);
        Assertions.assertEquals(expectedPosts, resultPosts);
        List<Float> resultAvgValues = new ArrayList<>();
        resultPosts.forEach(
                post -> {
                    Assertions.assertEquals(authorId, post.getAuthor().getId());
                    float sumValues = 0;
                    for (Like like : post.getLikes()) {
                        sumValues += like.getValue();
                    }
                    resultAvgValues.add(sumValues / post.getLikes().size());
                }
        );
        List<Float> expectedAvgValues = List.copyOf(resultAvgValues)
                .stream()
                .sorted(Comparator.reverseOrder())
                .toList();
        Assertions.assertEquals(expectedAvgValues, resultAvgValues);
    }

    @ParameterizedTest
    @MethodSource(
            "com.solvd.micro9.vkontaktah.service.TestDataProvider#getPost"
    )
    void verifyPostIsCreated(final Post postToCreate) {
        Mockito.when(postRepository.save(Mockito.any(Post.class)))
                .thenReturn(postToCreate);
        Mockito.when(
                        postRepository.setAuthor(
                                Mockito.anyString(),
                                Mockito.anyString()
                        )
                )
                .thenReturn(postToCreate);
        Post resultPost = postService.save(postToCreate);
        Assertions.assertNotNull(resultPost);
        Assertions.assertAll(
                () -> Assertions.assertNotNull(postToCreate.getId()),
                () -> Assertions.assertFalse(postToCreate.getIsEdited()),
                () -> Assertions.assertNull(postToCreate.getCreated())
        );
    }

    @Test
    void verifyLikeToPostIsSet() {
        Mockito.when(
                postRepository.addLike(
                        Mockito.anyString(),
                        Mockito.anyString(),
                        Mockito.anyFloat()
                )
        )
                .thenThrow(NoSuchElementException.class);
        Like like = Like.builder()
                .user(
                        User.builder()
                                .id("324342")
                                .build()
                )
                .value(5f)
                .build();
        Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> postService.like("xsasa", like)
        );
    }

}
