package com.solvd.micro9.vkontaktah.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import java.util.Objects;

@RelationshipProperties
@Data
@Builder
public class Like {

    @RelationshipId
    private Long id;

    private Float value;

    @TargetNode
    private User user;

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Like like = (Like) o;
        return Objects.equals(user.getId(), like.user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(user.getId());
    }

}
