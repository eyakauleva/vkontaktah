package com.solvd.micro9.vkontaktah.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Version;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node
@Data
@Builder
public class User {

    @Id
    private String id;

    private String login;

    private String firstName;

    private String lastName;

    private Gender gender;

    private Integer age;

    @Version
    private Long version;



}
