package com.solvd.micro9.vkontaktah.persistence;

import com.solvd.micro9.vkontaktah.domain.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

public interface UserRepository extends Neo4jRepository<User, String> {

    @Query("CREATE CONSTRAINT user_login_unique IF NOT EXISTS "
                + "FOR (user:User) REQUIRE user.login IS UNIQUE;")
    void createConstraints();

    @Query("CREATE RANGE INDEX user_id_idx IF NOT EXISTS FOR (user:User) ON (user.id);")
    void createIndexes();

}
