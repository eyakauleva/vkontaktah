package com.solvd.micro9.vkontaktah.integration.graphql;

import com.solvd.micro9.vkontaktah.web.TimeAsString;
import com.solvd.micro9.vkontaktah.web.WebConfig;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

@TestConfiguration
@EnableAutoConfiguration
@Import({WebConfig.class, TimeAsString.class})
public class ControllerITConfig {
}
