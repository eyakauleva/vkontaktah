package com.solvd.micro9.vkontaktah.persistence;

import com.solvd.micro9.vkontaktah.domain.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

public interface UserRepository extends Neo4jRepository<User, String> {

    @Query("CREATE CONSTRAINT user_login_required_unique IF NOT EXISTS "
                + "FOR (user:User) REQUIRE user.login IS NODE KEY; "
            + "CREATE CONSTRAINT user_firstname_required IF NOT EXISTS "
                + "FOR (user:User) REQUIRE user.firstName IS NOT NULL; "
            + "CREATE CONSTRAINT user_lastname_required IF NOT EXISTS "
                + "FOR (user:User) REQUIRE user.lastName IS NOT NULL; "
            + "CREATE CONSTRAINT user_age_float IF NOT EXISTS "
                + "FOR (user:User) REQUIRE user.age IS :: FLOAT; ")
    void createConstraints();

//    @Query("")
//    void createIndexes();

}
