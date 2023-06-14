package com.solvd.micro9.vkontaktah.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@RelationshipProperties
@Data
@Builder
public class Estimator {

    @RelationshipId
    private Long id;

    private Float value;

    @TargetNode
    private User user;

}
