package com.solvd.micro9.vkontaktah.integration.neo4j;

import com.solvd.micro9.vkontaktah.domain.Like;
import com.solvd.micro9.vkontaktah.domain.Post;
import com.solvd.micro9.vkontaktah.persistence.PostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.NoSuchElementException;
import java.util.UUID;

@SpringBootTest(classes = Neo4ITConfig.class)
class PostRepositoryIT extends Neo4jTestcontainers {

    @Autowired
    private PostRepository postRepository;

    @Test
    void verifyAllPostsAreReceived() {
        List<Post> posts = postRepository.findAll();
        Assertions.assertNotNull(posts);
        Assertions.assertEquals(postRepository.count(), posts.size());
    }

    @ParameterizedTest(
            name = "verifyPostsAreFoundByAuthor "
                    + "[authorId={0},pageOffset={1},pageLimit={2}]"
    )
    @CsvSource({
            "12, 0, 10"
    })
    void verifyPostsAreFoundByAuthor(
            final String authorId, final long pageOffset, final int pageLimit
    ) {
        List<Post> posts = postRepository.findByAuthor(authorId, pageOffset, pageLimit);
        Assertions.assertNotNull(posts);
        Assertions.assertTrue(posts.size() <= pageLimit);
        posts.forEach(post -> Assertions.assertEquals(authorId, post.getAuthor().getId()));
    }

    @ParameterizedTest(name = "verifyPostsAreNotFoundByAuthor [authorId={0}]")
    @ValueSource(strings = {"634243HJGDGSD120000000000"})
    void verifyPostsAreNotFoundByAuthor(final String authorId) {
        List<Post> posts =
                Assertions.assertDoesNotThrow(
                        () -> postRepository.findByAuthor(authorId, 0L, 100)
                );
        Assertions.assertTrue(posts == null || posts.size() == 0);
    }

    @ParameterizedTest(
            name = "verifyPostsAreFoundByLiker [likerId={0},pageOffset={1},pageLimit={2}]"
    )
    @CsvSource({
            "11, 0, 10"
    })
    void verifyPostsAreFoundByLiker(
            final String likerId, final long pageOffset, final int pageLimit
    ) {
        List<Post> posts = postRepository.findByLiker(likerId, pageOffset, pageLimit);
        Assertions.assertNotNull(posts);
        Assertions.assertTrue(posts.size() <= pageLimit);
        posts.forEach(post -> {
            boolean userLiked = false;
            for (Like like : post.getLikes()) {
                if (like.getUser().getId().equals(likerId)) {
                    userLiked = true;
                    break;
                }
            }
            Assertions.assertTrue(userLiked);
        });
    }

    @ParameterizedTest(name = "verifyAuthorsTopPostsAreFound [authorId={0},count={1}]")
    @CsvSource({
            "12, 5"
    })
    void verifyAuthorsTopPostsAreFound(final String authorId, final int count) {
        List<Post> posts = postRepository.findAuthorTop(authorId, count);
        Assertions.assertNotNull(posts);
        Assertions.assertTrue(posts.size() <= count);
        List<Float> averageLikeValues = new ArrayList<>();
        posts.forEach(post -> {
            Assertions.assertEquals(authorId, post.getAuthor().getId());
            float valuesSum = 0;
            for (Like like : post.getLikes()) {
                valuesSum += like.getValue();
            }
            averageLikeValues.add(valuesSum / post.getLikes().size());
        });
        List<Float> orderedAverageLikeValues = List.copyOf(averageLikeValues)
                .stream()
                .sorted(Comparator.reverseOrder())
                .toList();
        Assertions.assertEquals(orderedAverageLikeValues, averageLikeValues);
    }

    @ParameterizedTest(name = "verifyPostIsFoundById [postId={0},text={1}]")
    @CsvSource({
            "101, I like dogs"
    })
    void verifyPostIsFoundById(final String postId, final String text) {
        Optional<Post> post = postRepository.findById(postId);
        Assertions.assertNotNull(post);
        Assertions.assertTrue(post.isPresent());
        Assertions.assertAll(
                () -> Assertions.assertEquals(postId, post.get().getId()),
                () -> Assertions.assertEquals(text, post.get().getText())
        );
    }


    @ParameterizedTest(name = "verifyPostIsSaved [postId={0},text={1}]")
    @CsvSource({
            "9999, fake post for test"
    })
    void verifyPostIsSaved(final String postId, final String text) {
        Post post = Post.builder()
                .id(postId)
                .text(text)
                .isEdited(false)
                .author(null)
                .build();
        postRepository.save(post);
        this.verifyPostIsFoundById(postId, text);
    }

    @ParameterizedTest(name = "verifyAuthorIsSetToPost [authorId={0}]")
    @ValueSource(strings = {"10"})
    void verifyAuthorIsSetToPost(final String authorId) {
        String postId = UUID.randomUUID().toString();
        Post post = Post.builder()
                .id(postId)
                .text("some text")
                .isEdited(false)
                .author(null)
                .build();
        postRepository.save(post);
        Post postWithSetAuthor = postRepository.setAuthor(authorId, postId);
        Assertions.assertNotNull(postWithSetAuthor);
        Assertions.assertNotNull(postWithSetAuthor.getAuthor());
        Assertions.assertAll(
                () -> Assertions.assertEquals(postId, postWithSetAuthor.getId()),
                () -> Assertions.assertEquals(
                        authorId, postWithSetAuthor.getAuthor().getId()
                )
        );
    }

    @ParameterizedTest(name = "verifyPostIsLiked [userId={0},postId={1},value={2}]")
    @CsvSource({
            "10, 101, 7.5"
    })
    void verifyPostIsLiked(final String userId, final String postId, final Float value) {
        Post post = Assertions.assertDoesNotThrow(
                () -> postRepository.addLike(userId, postId, value)
        );
        Assertions.assertNotNull(post);
        Assertions.assertNotNull(post.getLikes());
        Assertions.assertTrue(post.getLikes().size() > 0);
        boolean isLikeSet = false;
        for (Like like : post.getLikes()) {
            if (userId.equals(like.getUser().getId())) {
                Assertions.assertEquals(value, like.getValue());
                isLikeSet = true;
                break;
            }
        }
        Assertions.assertTrue(isLikeSet);
    }

    @ParameterizedTest(name = "verifyPostIsNotLiked [postId={0}]")
    @ValueSource(strings = {"634243HJGDGSD120000000000"})
    void verifyPostIsNotLiked(final String postId) {
        Assertions.assertThrows(
                NoSuchElementException.class,
                () -> postRepository.addLike("10", postId, 9.5f)
        );
    }

}
