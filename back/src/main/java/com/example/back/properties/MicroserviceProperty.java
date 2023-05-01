package com.example.back.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "microservice")
@Data
public class MicroserviceProperty {
    private Kafka kafka;

    @Data
    private static class Kafka {
        private Topic topics;
        private String projectionGroupId;
        private String groupId;
        private String defaultConcurrency;

        @Data
        private static class Topic {
            private String cardEventStore;
        }
    }
}
