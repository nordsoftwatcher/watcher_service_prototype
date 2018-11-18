package ru.nord.backend.infrastructure.config;

import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.jndi.JndiLocatorDelegate;
import ru.nord.backend.infrastructure.support.CustomJndiPropertySource;

public class EnvironmentPostProcessor implements org.springframework.boot.env.EnvironmentPostProcessor
{
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application)
    {
        if (JndiLocatorDelegate.isDefaultJndiEnvironmentAvailable()) {
            environment.getPropertySources().addLast(new CustomJndiPropertySource("customJndiProperties", environment));
        }
    }
}
