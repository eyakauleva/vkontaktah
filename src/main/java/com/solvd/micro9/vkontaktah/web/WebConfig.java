package com.solvd.micro9.vkontaktah.web;

import graphql.schema.GraphQLScalarType;
import graphql.validation.rules.OnValidationErrorStrategy;
import graphql.validation.rules.ValidationRules;
import graphql.validation.schemawiring.ValidationSchemaWiring;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public GraphQLScalarType localDateTimeScalar(
            final TimeAsString coercing
    ) {
        return GraphQLScalarType.newScalar()
                .name("LocalDateTime")
                .description("Java 8 LocalDateTime as scalar")
                .coercing(coercing)
                .build();
    }

    @Bean
    public ValidationSchemaWiring schemaWiring() {
        ValidationRules validationRules = ValidationRules.newValidationRules()
                .onValidationErrorStrategy(OnValidationErrorStrategy.RETURN_NULL)
                .build();
        return new ValidationSchemaWiring(validationRules);
    }

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer(
            final GraphQLScalarType scalarType,
            final ValidationSchemaWiring schemaWiring
    ) {
        return wiringBuilder -> wiringBuilder
                .scalar(scalarType)
                .directiveWiring(schemaWiring);
    }

}
