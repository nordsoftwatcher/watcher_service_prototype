package ru.nord.siwatch.backend.facade.operator.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.nord.siwatch.backend.facade.operator.api.v1.ApiBase;


@Configuration
public class WebMvcConfig implements WebMvcConfigurer
{
    @Override
    public void addCorsMappings(CorsRegistry registry)
    {
        registry.addMapping(ApiBase.PATH+"**")
            .allowedOrigins(CorsConfiguration.ALL)
            .allowedMethods("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH")
            .allowCredentials(true)
            .allowedHeaders("Authorization", "Cache-Control", "Content-Type")
            .maxAge(3600);
    }

    @Override
    public void addFormatters(FormatterRegistry registry)
    {
    }
}
