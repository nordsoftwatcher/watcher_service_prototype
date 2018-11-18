package ru.nord.siwatch.backend.connectors.locationmonitoring;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.net.URI;

@Component
@Validated
@ConfigurationProperties(prefix = "connector.location-monitoring")
@Getter @Setter
public class LocationMonitoringConnectorSettings
{
    @NotNull
    private URI serviceUrl;
}
