package de.ghostnet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point of the Spring Boot application.
 *
 * Database schema initialization and test data seeding
 * are handled exclusively via Flyway migrations
 * located in {@code db/migration}.
 */
@SpringBootApplication
public class GhostNetApplication {

    public static void main(String[] args) {
        SpringApplication.run(GhostNetApplication.class, args);
    }
}
