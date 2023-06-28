package com.solvd.micro9.vkontaktah.integration.graphql;

import com.solvd.micro9.vkontaktah.domain.Like;
import com.solvd.micro9.vkontaktah.domain.Post;
import com.solvd.micro9.vkontaktah.service.PostService;
import com.solvd.micro9.vkontaktah.web.controller.PostController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.test.tester.GraphQlTester;

import java.util.List;

@GraphQlTest(PostController.class)
@Import(ControllerITConfig.class)
public class PostControllerIT {

    @Autowired
    private GraphQlTester tester;

    @MockBean
    private PostService postService;

    @ParameterizedTest
    @MethodSource("com.solvd.micro9.vkontaktah.TestDataProvider#getPosts")
    void verifyAllPostsAreReceived(final List<Post> postsToMock) {
        String query = "{ getAllPosts(size:10,page:0) { id text } }";
        Mockito.when(postService.getAll(Mockito.any(Pageable.class)))
                .thenReturn(postsToMock);
        List<Post> resultPosts = this.tester.document(query)
                .execute()
                .path("data.getAllPosts[*]")
                .entityList(Post.class)
                .get();
        Mockito.verify(postService, Mockito.times(1))
                .getAll(Mockito.any(Pageable.class));
        Assertions.assertNotNull(resultPosts);
        Assertions.assertTrue(resultPosts.size() > 0);
    }

    @ParameterizedTest
    @MethodSource(
            "com.solvd.micro9.vkontaktah.TestDataProvider#getPostsWithAuthorId"
    )
    void verifyPostsAreFoundByAuthor(final List<Post> postsToMock, final String authorId) {
        String query = "{ findPostsByAuthor(authorId: \"" + authorId
                + "\", cursor: \"aaaa\", pageSize: 10) { id, text, author { id } } }";
        Mockito.when(
                postService.findByAuthor(
                        Mockito.anyString(), Mockito.anyString(), Mockito.anyInt()
                )
        )
                .thenReturn(postsToMock);
        List<Post> resultPosts = this.tester.document(query)
                .execute()
                .path("data.findPostsByAuthor[*]")
                .entityList(Post.class)
                .get();
        Mockito.verify(postService, Mockito.times(1))
                .findByAuthor(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt());
        Assertions.assertNotNull(resultPosts);
        Assertions.assertTrue(resultPosts.size() > 0);
    }

    @ParameterizedTest
    @MethodSource(
            "com.solvd.micro9.vkontaktah.TestDataProvider#getPostsWithLikerId"
    )
    void verifyPostsAreFoundByLiker(final List<Post> postsToMock, final String likerId) {
        String query = "{ findPostsByLiker(likerId: \"" + likerId
                + "\", cursor: \"aaaa\", pageSize: 10) "
                + "{ id, text, likes { user { id } } } }";
        Mockito.when(
                postService.findByLiker(
                        Mockito.anyString(), Mockito.anyString(), Mockito.anyInt()
                )
        )
                .thenReturn(postsToMock);
        List<Post> resultPosts = this.tester.document(query)
                .execute()
                .path("data.findPostsByLiker[*]")
                .entityList(Post.class)
                .get();
        Mockito.verify(postService, Mockito.times(1))
                .findByLiker(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt());
        Assertions.assertNotNull(resultPosts);
        Assertions.assertTrue(resultPosts.size() > 0);
    }

    @ParameterizedTest
    // CSOFF: LineLength
    @MethodSource(
            "com.solvd.micro9.vkontaktah.TestDataProvider#getSortedByAvgValuePostsWithAuthorId"
    )
    // CSON: LineLength
    void verifyAuthorsTopPostsAreFound(
            final List<Post> postsToMock, final String authorId
    ) {
        String query = "{ findAuthorTopPosts(authorId: \"" + authorId
                + "\", count: 5) { id, text, author { id } } }";
        Mockito.when(postService.findAuthorTop(Mockito.anyString(), Mockito.anyInt()))
                .thenReturn(postsToMock);
        List<Post> resultPosts = this.tester.document(query)
                .execute()
                .path("data.findAuthorTopPosts[*]")
                .entityList(Post.class)
                .get();
        Mockito.verify(postService, Mockito.times(1))
                .findAuthorTop(Mockito.anyString(), Mockito.anyInt());
        Assertions.assertNotNull(resultPosts);
        Assertions.assertTrue(resultPosts.size() > 0);
    }

    @ParameterizedTest
    // CSOFF: LineLength
    @MethodSource(
            "com.solvd.micro9.vkontaktah.TestDataProvider#getPostsToCreateAndToMock"
    )
    // CSON: LineLength
    void verifyPostIsCreated(final Post postToCreate, final Post postToMock) {
        String query = "mutation { savePost(post: {text: \"" + postToCreate.getText()
                + "\", author: {id: \"" + postToCreate.getAuthor().getId()
                + "\"} }) { id, text, author { id } } }";
        Mockito.when(postService.save(Mockito.any(Post.class)))
                .thenReturn(postToMock);
        Post resultPost = this.tester.document(query)
                .execute()
                .path("data.savePost")
                .entity(Post.class)
                .get();
        Mockito.verify(postService, Mockito.times(1))
                .save(Mockito.any(Post.class));
        Assertions.assertNotNull(resultPost);
    }

    @ParameterizedTest
    @MethodSource(
            "com.solvd.micro9.vkontaktah.TestDataProvider#getLikeAndPostToMock"
    )
    void verifyLikeIsSet(final Like like, final Post postToMock) {
        String query = "mutation { likePost(postId: \"" + postToMock.getId()
                + "\", like: {value: " + like.getValue()
                + ", user: {id: \"" + like.getUser().getId()
                + "\" } } ) { id, text, likes { user { id } } } }";
        Mockito.when(postService.like(Mockito.anyString(), Mockito.any(Like.class)))
                .thenReturn(postToMock);
        Post resultPost = this.tester.document(query)
                .execute()
                .path("data.likePost")
                .entity(Post.class)
                .get();
        Mockito.verify(postService, Mockito.times(1))
                .like(Mockito.anyString(), Mockito.any(Like.class));
        Assertions.assertNotNull(resultPost);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "null, 10, userId",
            "'', 10, userId",
            "postId, null, userId",
            "postId, -10, userId",
            "postId, 11, userId",
            "postId, 100, userId",
            "postId, 8.555, userId",
            "postId, 10, null",
            "postId, 10, ''",
    },
            nullValues = {"null"}
    )
    void verifyArgsAreValidatedWhenLikeIsSet(
            final String postId, final Float value, final String userId
    ) {
        String query = "mutation { likePost(postId: \"" + postId
                + "\", like: {value: " + value
                + ", user: {id: \"" + userId
                + "\" } } ) { id, text, likes { user { id } } } }";
        Assertions.assertThrows(
                Throwable.class,
                () -> this.tester.document(query)
                        .execute()
                        .path("data.likePost")
                        .entity(Post.class)
                        .get()
        );
    }

}
