package com.solvd.micro9.vkontaktah.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node
@Data
@Builder
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String login;

    private String firstName;

    private String lastName;

    private Gender gender;

    private Integer age;

}
