package com.solvd.micro9.vkontaktah.persistence;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.EnableNeo4jAuditing;

@Configuration
@RequiredArgsConstructor
@EnableNeo4jAuditing
public class PersistenceConfig {

    final public UserRepository userRepository;
    final public PostRepository postRepository;

    @PostConstruct
    public void init() {
        userRepository.createConstraints();
        postRepository.createConstraints();
    }

}
