package assignment.proxyservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.Logbook;

import static org.zalando.logbook.Conditions.exclude;
import static org.zalando.logbook.Conditions.requestTo;
import static org.zalando.logbook.json.JsonPathBodyFilters.jsonPath;

@Configuration
public class LogbookConfig {
    @Bean
    public Logbook logbook() {
        return Logbook.builder()
                .condition(exclude(
                        requestTo("/h2-console/**"),
                        requestTo("/swagger-ui/**"),
                        requestTo("/v3/api-docs/**")

                ))
                .bodyFilter(jsonPath("$.password").replace("<secret>"))
                .build();
    }
}
