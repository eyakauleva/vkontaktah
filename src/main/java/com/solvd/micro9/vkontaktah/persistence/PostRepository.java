package com.solvd.micro9.vkontaktah.persistence;

import com.solvd.micro9.vkontaktah.domain.Post;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends Neo4jRepository<Post, String> {

    @Query("MATCH (author:User {id: $userId}), (post:Post {id: $postId}) "
            + "CREATE (author)-[relationship:CREATED]->(post) "
            + "RETURN author, post, relationship")
    Post setAuthor(@Param("userId") String userId,
                   @Param("postId") String postId);

    @Query("MATCH (liker:User {id: $userId}), (post:Post {id: $postId}) "
            + "CREATE (liker)-[relationship:LIKED {value: $value}]->(post) "
            + "WITH post "
            + "MATCH (allRelatedUsers:User)-[allRelationships]-(post) "
            + "RETURN post, collect(allRelationships), collect(allRelatedUsers)")
    Post addLike(@Param("userId") String userId,
                 @Param("postId") String postId,
                 @Param("value") Float value);

}
