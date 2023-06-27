package com.solvd.micro9.vkontaktah.service;

import com.solvd.micro9.vkontaktah.domain.Like;
import com.solvd.micro9.vkontaktah.domain.Post;
import com.solvd.micro9.vkontaktah.domain.User;
import org.junit.jupiter.params.provider.Arguments;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

public final class TestDataProvider {

    private TestDataProvider() {
    }

    public static Stream<List<User>> getUsers() {
        return Stream.of(
                List.of(
                        User.builder()
                                .id(UUID.randomUUID().toString())
                                .login("login1")
                                .build(),
                        User.builder()
                                .id(UUID.randomUUID().toString())
                                .login("login2")
                                .build()
                )
        );
    }

    public static Stream<User> getUser() {
        return Stream.of(
                User.builder()
                        .login("MyLogin")
                        .firstName("MyName")
                        .lastName("MySurname")
                        .age(21)
                        .build()
        );
    }

    public static Stream<Arguments> getPostsToCreateAndToMock() {
        Post postToCreate = Post.builder()
                .text("some text 1")
                .author(
                        User.builder()
                                .id("62423")
                                .build()
                )
                .build();
        LocalDateTime localDateTime = LocalDateTime.now();
        return Stream.of(
                Arguments.of(
                        postToCreate,
                        Post.builder()
                                .id("111")
                                .text(postToCreate.getText())
                                .isEdited(false)
                                .created(localDateTime)
                                .lastModified(localDateTime)
                                .version(0L)
                                .author(postToCreate.getAuthor())
                                .build()
                )
        );
    }

    public static Stream<List<Post>> getPosts() {
        return Stream.of(
                List.of(
                        Post.builder()
                                .id(UUID.randomUUID().toString())
                                .text("some text 1")
                                .build(),
                        Post.builder()
                                .id(UUID.randomUUID().toString())
                                .text("some text 2")
                                .build()
                )
        );
    }

    public static Stream<Arguments> getPostsWithAuthorId() {
        String authorId = "2716736182";
        return Stream.of(
                Arguments.of(
                        List.of(
                                Post.builder()
                                        .id(UUID.randomUUID().toString())
                                        .text("some text 1")
                                        .author(
                                                User.builder()
                                                        .id(authorId)
                                                        .build()
                                        )
                                        .build(),
                                Post.builder()
                                        .id(UUID.randomUUID().toString())
                                        .text("some text 2")
                                        .author(
                                                User.builder()
                                                        .id(authorId)
                                                        .build()
                                        )
                                        .build()
                        ),
                        authorId
                )
        );
    }

    public static Stream<Arguments> getPostsWithLikerId() {
        String likerId = "2716736182";
        return Stream.of(
                Arguments.of(
                        List.of(
                                Post.builder()
                                        .id(UUID.randomUUID().toString())
                                        .text("some text 1")
                                        .likes(
                                                Set.of(
                                                        getLikeWithLikerId(likerId, 9.0f)
                                                )
                                        )
                                        .build(),
                                Post.builder()
                                        .id(UUID.randomUUID().toString())
                                        .text("some text 2")
                                        .likes(
                                                Set.of(
                                                        getLikeWithLikerId(likerId, 8.8f)
                                                )
                                        )
                                        .build()
                        ),
                        likerId
                )
        );
    }

    public static Stream<Arguments> getSortedByAvgValuePostsWithAuthorId() {
        String authorId = "2716736182";
        return Stream.of(
                Arguments.of(
                        List.of(
                                Post.builder()
                                        .id(UUID.randomUUID().toString())
                                        .text("some text 1")
                                        .author(
                                                User.builder()
                                                        .id(authorId)
                                                        .build()
                                        )
                                        .likes(
                                                Set.of(
                                                        getLikeWithLikerId("3425232", 8.8f)
                                                )
                                        )
                                        .build(),
                                Post.builder()
                                        .id(UUID.randomUUID().toString())
                                        .text("some text 2")
                                        .author(
                                                User.builder()
                                                        .id(authorId)
                                                        .build()
                                        )
                                        .likes(
                                                Set.of(
                                                        getLikeWithLikerId(
                                                                "64623423",
                                                                7.0f
                                                        )
                                                )
                                        )
                                        .build()
                        ),
                        authorId
                )
        );
    }

    public static Stream<Arguments> getLikeAndPostToMock() {
        return Stream.of(
                Arguments.of(
                        Like.builder()
                                .user(
                                        User.builder()
                                                .id("324342")
                                                .build()
                                )
                                .value(5f)
                                .build(),
                        Post.builder()
                                .id(UUID.randomUUID().toString())
                                .text("some text 1")
                                .build()
                )
        );
    }

    private static Like getLikeWithLikerId(final String likerId, final Float value) {
        return Like.builder()
                .user(
                        User.builder()
                                .id(likerId)
                                .build()
                )
                .value(value)
                .build();
    }

}
