package ru.nord.siwatch.backend.connectors.networkmonitoring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class NetworkMonitoringConnector {

    private final RestTemplate restTemplate;

    public NetworkMonitoringConnector(RestTemplateBuilder restTemplateBuilder, NetworkMonitoringConnectorSettings settings) {
        this.restTemplate = restTemplateBuilder.rootUri(settings.getServiceUrl().toString()).build();
    }
}
