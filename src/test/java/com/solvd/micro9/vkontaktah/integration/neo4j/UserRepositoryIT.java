package com.solvd.micro9.vkontaktah.integration.neo4j;

import com.solvd.micro9.vkontaktah.domain.User;
import com.solvd.micro9.vkontaktah.persistence.UserRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.neo4j.core.Neo4jTemplate;

import java.util.List;

@SpringBootTest(classes = Neo4ITConfig.class)
public class UserRepositoryIT extends Neo4jTestcontainers {

    private static Neo4jTemplate neo4jTemplate;

    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    @SneakyThrows
    static void init() {
        String filesList = NEO4J_CONTAINER.execInContainer("ls","/var/lib/neo4j/db_init/").getStdout();
        System.out.println("---10: " + filesList);

        String filesList2 = NEO4J_CONTAINER.execInContainer("ls","/var/lib/neo4j/conf/").getStdout();
        System.out.println("---11: " + filesList2);
    }

    @Test
    void verifyAllUsersAreReceived() {
        List<User> users = userRepository.findAll();
        System.out.println("---100: " + users);
        Assertions.assertTrue(users.size() > 0);
    }

}
