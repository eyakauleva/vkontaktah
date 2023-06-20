package com.solvd.micro9.vkontaktah.persistence;

import com.solvd.micro9.vkontaktah.domain.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

public interface UserRepository extends Neo4jRepository<User, String> {

    @Query("CREATE CONSTRAINT user_id_unique IF NOT EXISTS "
            + "FOR (user:User) REQUIRE user.id IS UNIQUE")
    void createIdConstraint();

    @Query("CREATE CONSTRAINT user_login_unique IF NOT EXISTS "
                + "FOR (user:User) REQUIRE user.login IS UNIQUE")
    void createLoginConstraint();

}
