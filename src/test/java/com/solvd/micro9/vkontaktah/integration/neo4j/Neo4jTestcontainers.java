package com.solvd.micro9.vkontaktah.integration.neo4j;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.containers.Neo4jLabsPlugin;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

@Testcontainers
abstract class Neo4jTestcontainers {

    @Container
    private static final Neo4jContainer<?> NEO4J_CONTAINER =
            new Neo4jContainer<>(DockerImageName.parse("neo4j:5.8.0"))
                    .withRandomPassword()
                    .withLabsPlugins(Neo4jLabsPlugin.APOC)
                    .withEnv("dbms.directories.import", "/")
                    .withEnv("dbms.security.procedures.allowlist", "apoc.cypher.*")
                    .withEnv("dbms.security.procedures.unrestricted", "apoc.cypher.*")
                    .withCopyFileToContainer(
                            MountableFile.forClasspathResource("apoc-5.8.1-extended.jar"),
                            "/var/lib/neo4j/plugins/"
                    )
                    .withCopyFileToContainer(
                            MountableFile.forClasspathResource("/schema.cypher"),
                            "/var/lib/neo4j/db_init/"
                    )
                    .withCopyFileToContainer(
                            MountableFile.forClasspathResource("/apoc.conf"),
                            "/var/lib/neo4j/conf/"
                    );

    @DynamicPropertySource
    static void neo4jProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.neo4j.uri", NEO4J_CONTAINER::getBoltUrl);
        registry.add("spring.neo4j.authentication.username", () -> "neo4j");
        registry.add(
                "spring.neo4j.authentication.password",
                NEO4J_CONTAINER::getAdminPassword
        );
    }

}
