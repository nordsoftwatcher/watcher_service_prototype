package ru.nord.siwatch.backend.services.memorymonitoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "ru.nord.siwatch.backend.services")
@EntityScan(basePackages = "ru.nord.siwatch.backend.services.memorymonitoring.entities")
@EnableJpaRepositories(basePackages = "ru.nord.siwatch.backend.services.memorymonitoring.repositories")
public class Application extends SpringBootServletInitializer
{
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
}
