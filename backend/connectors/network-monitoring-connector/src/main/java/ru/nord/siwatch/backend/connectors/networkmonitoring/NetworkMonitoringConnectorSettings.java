package ru.nord.siwatch.backend.connectors.networkmonitoring;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.net.URI;

@Component
@Validated
@ConfigurationProperties(prefix = "connector.network-monitoring")
@Getter
@Setter
public class NetworkMonitoringConnectorSettings {

    @NotNull
    private URI serviceUrl;
}
