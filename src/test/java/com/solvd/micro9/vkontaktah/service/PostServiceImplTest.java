package com.solvd.micro9.vkontaktah.service;

import com.solvd.micro9.vkontaktah.domain.Like;
import com.solvd.micro9.vkontaktah.domain.Post;
import com.solvd.micro9.vkontaktah.domain.exception.ResourceNotFoundException;
import com.solvd.micro9.vkontaktah.persistence.PostRepository;
import com.solvd.micro9.vkontaktah.service.impl.PostServiceImpl;
import org.junit.jupiter.api.Assertions;
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
    void verifyAllPostsAreReceived(final List<Post> postsToMock) {
        Pageable pageable = PageRequest.of(0, 100);
        Mockito.when(postRepository.findAll(Mockito.any(Pageable.class)))
                .thenReturn(
                        new PageImpl<>(
                                postsToMock,
                                pageable,
                                postsToMock.size()
                        )
                );
        List<Post> resultPosts = postService.getAll(pageable);
        Mockito.verify(postRepository, Mockito.times(1))
                .findAll(Mockito.any(Pageable.class));
        Assertions.assertNotNull(resultPosts);
    }

    @ParameterizedTest
    @MethodSource(
            "com.solvd.micro9.vkontaktah.service.TestDataProvider#getPostsWithAuthorId"
    )
    void verifyPostsAreFoundByAuthor(
            final List<Post> postsToMock, final String authorId
    ) {
        String cursor = "0";
        int pageSize = 100;
        Mockito.when(postRepository.findByAuthor(authorId, cursor, pageSize))
                .thenReturn(postsToMock);
        List<Post> resultPosts = postService.findByAuthor(authorId, cursor, pageSize);
        Mockito.verify(postRepository, Mockito.times(1))
                .findByAuthor(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt());
        Assertions.assertNotNull(resultPosts);
    }

    @ParameterizedTest
    @MethodSource(
            "com.solvd.micro9.vkontaktah.service.TestDataProvider#getPostsWithLikerId"
    )
    void verifyPostsAreFoundByLiker(final List<Post> postsToMock, final String likerId) {
        String cursor = "0";
        int pageSize = 100;
        Mockito.when(postRepository.findByLiker(likerId, cursor, pageSize))
                .thenReturn(postsToMock);
        List<Post> resultPosts = postService.findByLiker(likerId, cursor, pageSize);
        Mockito.verify(postRepository, Mockito.times(1))
                .findByLiker(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt());
        Assertions.assertNotNull(resultPosts);
    }

    @ParameterizedTest
    // CSOFF: LineLength
    @MethodSource(
            "com.solvd.micro9.vkontaktah.service.TestDataProvider#getSortedByAvgValuePostsWithAuthorId"
    )
    // CSON: LineLength
    void verifyTopAuthorsPostsAreFound(
            final List<Post> postsToMock, final String authorId
    ) {
        int count = 5;
        Mockito.when(postRepository.findAuthorTop(authorId, count))
                .thenReturn(postsToMock);
        List<Post> resultPosts = postService.findAuthorTop(authorId, count);
        Mockito.verify(postRepository, Mockito.times(1))
                .findAuthorTop(Mockito.anyString(), Mockito.anyInt());
        Assertions.assertNotNull(resultPosts);
    }

    @ParameterizedTest
    // CSOFF: LineLength
    @MethodSource(
            "com.solvd.micro9.vkontaktah.service.TestDataProvider#getPostsToCreateAndToMock"
    )
    // CSON: LineLength
    void verifyPostIsCreated(final Post postToCreate, final Post postToMock) {
        Mockito.when(postRepository.save(Mockito.any(Post.class)))
                .thenReturn(postToMock);
        Mockito.when(
                        postRepository.setAuthor(
                                Mockito.anyString(),
                                Mockito.anyString()
                        )
                )
                .thenReturn(postToMock);
        Post resultPost = postService.save(postToCreate);
        Mockito.verify(postRepository, Mockito.times(1))
                .save(Mockito.any(Post.class));
        Mockito.verify(postRepository, Mockito.times(1))
                .setAuthor(Mockito.anyString(), Mockito.anyString());
        Assertions.assertNotNull(resultPost);
        Assertions.assertAll(
                () -> Assertions.assertNotNull(postToCreate.getId()),
                () -> Assertions.assertFalse(postToCreate.getIsEdited()),
                () -> Assertions.assertNull(postToCreate.getAuthor())
        );
    }

    @ParameterizedTest
    @MethodSource(
            "com.solvd.micro9.vkontaktah.service.TestDataProvider#getLikeAndPostToMock"
    )
    void verifyLikeToPostIsSet(final Like like, final Post postToMock) {
        Mockito.when(
                        postRepository.addLike(
                                Mockito.anyString(),
                                Mockito.anyString(),
                                Mockito.anyFloat()
                        )
                )
                .thenReturn(postToMock);
        Post resultPost = postService.like(postToMock.getId(), like);
        Mockito.verify(postRepository, Mockito.times(1))
                .addLike(Mockito.anyString(), Mockito.anyString(), Mockito.anyFloat());
        Assertions.assertNotNull(resultPost);
    }

    @ParameterizedTest
    @MethodSource(
            "com.solvd.micro9.vkontaktah.service.TestDataProvider#getLikeAndPostToMock"
    )
    void verifyLikeToPostThrowsException(final Like like) {
        Mockito.when(
                        postRepository.addLike(
                                Mockito.anyString(),
                                Mockito.anyString(),
                                Mockito.anyFloat()
                        )
                )
                .thenThrow(NoSuchElementException.class);
        Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> postService.like("xsasa", like)
        );
    }

}
