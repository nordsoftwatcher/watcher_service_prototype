package ru.nord.siwatch.backend.services.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.nord.siwatch.backend.services.common.api.ApiBase;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

@Configuration
@EnableSwagger2
@Import(springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {
    @Value("${spring.application.name:App}")
    private String applicationName;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(new ApiInfo(
                "SiWatch.Backend."+applicationName,
                applicationName+" REST API",
                "1.0",
                null,
                null,
                null,
                null,
                Collections.emptyList()
            ))
            .protocols(new HashSet<>(Arrays.asList("http", "https")))
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.ant(ApiBase.PATH+"**"))
            .build();
    }
}
