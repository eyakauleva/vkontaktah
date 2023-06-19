package com.solvd.micro9.vkontaktah.persistence;

import com.solvd.micro9.vkontaktah.domain.Post;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends Neo4jRepository<Post, String> {

    @Query("MATCH (author:User {id: $userId}), (post:Post {id: $postId}) "
            + "CREATE (author)-[relationship:CREATED]->(post) "
            + "RETURN author, post, relationship")
    Post setAuthor(@Param("userId") String userId,
                   @Param("postId") String postId);

    @Query("MATCH (liker:User {id: $userId}), (post:Post {id: $postId}) "
            + "MERGE (liker)-[relationship:LIKED]->(post) "
            + "ON CREATE SET relationship.value=$value "
            + "ON MATCH SET relationship.value=$value "
            + "WITH post "
            + "MATCH postWithRelations=(:User)-[]-(post) "
            + "RETURN collect(postWithRelations)")
    Post addLike(@Param("userId") String userId,
                 @Param("postId") String postId,
                 @Param("value") Float value);

    @Query("MATCH (author:User {id: $authorId})-[relationship:CREATED]->(posts:Post) "
            + "WITH posts "
            + "MATCH (allRelatedUsers:User)-[allRelationships]-(posts) "
            + "RETURN DISTINCT posts, collect(allRelationships), collect(allRelatedUsers) "
            + "ORDER BY posts.created "
            + "SKIP $offset LIMIT $limit")
    List<Post> findByAuthor(@Param("authorId") String authorId,
                            @Param("offset") Long offset,
                            @Param("limit") Integer limit);

    @Query("MATCH (author:User {id: $likerId})-[relationship:LIKED]->(posts:Post) "
            + "WITH posts "
            + "MATCH (allRelatedUsers:User)-[allRelationships]-(posts) "
            + "RETURN DISTINCT posts, collect(allRelationships), collect(allRelatedUsers) "
            + "ORDER BY posts.created "
            + "SKIP $offset LIMIT $limit")
    List<Post> findByLiker(@Param("likerId") String likerId,
                           @Param("offset") Long offset,
                           @Param("limit") Integer limit);

    @Query("MATCH (:User {id: $authorId})-[:CREATED]->(posts:Post) "
            + "WITH posts "
            + "MATCH (:User)-[likes:LIKED]->(posts) "
            + "WITH posts, avg(toFloat(likes.value)) AS avgValue "
            + "ORDER BY avgValue DESC "
            + "LIMIT $count "
            + "MATCH (allRelatedUsers:User)-[allRelationships]->(posts) "
            + "RETURN DISTINCT posts, collect(allRelatedUsers), collect(allRelationships)")
    List<Post> findAuthorTop(@Param("authorId") String authorId,
                             @Param("count") Integer count);

//    @Query("")
//    void createIndexes();

}
