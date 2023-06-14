package com.solvd.micro9.vkontaktah.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDateTime;
import java.util.Set;

@Node
@Data
@Builder
public class Post {

    @Id
    @GeneratedValue
    private Long id;

    private String text;

    private Boolean isEdited;

    @CreatedDate
    private LocalDateTime created;

    @LastModifiedDate
    private LocalDateTime lastModified;

    @Relationship(type = "CREATED", direction = Relationship.Direction.INCOMING)
    private User author;

    @Relationship(type = "LIKED", direction = Relationship.Direction.INCOMING)
    private Set<Estimator> estimates;

}
