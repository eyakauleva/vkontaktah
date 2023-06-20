package com.solvd.micro9.vkontaktah.persistence;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.EnableNeo4jAuditing;

@Configuration
@RequiredArgsConstructor
@EnableNeo4jAuditing
public class PersistenceConfig {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @PostConstruct
    public void init() {
        userRepository.createIdConstraint();
        userRepository.createLoginConstraint();
        postRepository.createIdConstraint();
    }

}
