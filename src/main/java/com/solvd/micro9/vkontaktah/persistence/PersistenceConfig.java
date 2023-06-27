package com.solvd.micro9.vkontaktah.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.EnableNeo4jAuditing;

@Configuration
@RequiredArgsConstructor
@EnableNeo4jAuditing
public class PersistenceConfig {
}
