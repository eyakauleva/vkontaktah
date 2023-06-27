package com.solvd.micro9.vkontaktah.integration.neo4j;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.graphql.GraphQlAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;

@TestConfiguration
@EnableAutoConfiguration(exclude = GraphQlAutoConfiguration.class)
@ComponentScan("com.solvd.micro9.vkontaktah.persistence")
public class Neo4ITConfig {
}
