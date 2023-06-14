package com.solvd.micro9.vkontaktah.persistence;

import com.solvd.micro9.vkontaktah.domain.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface UserRepository extends Neo4jRepository<User, Long> {
}
