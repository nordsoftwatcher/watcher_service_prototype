package ru.nord.siwatch.backend.connectors.memorymonitoring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class MemoryMonitoringConnector {

    private final RestTemplate restTemplate;

    public MemoryMonitoringConnector(RestTemplateBuilder restTemplateBuilder, MemoryMonitoringConnectorSettings settings) {
        this.restTemplate = restTemplateBuilder.rootUri(settings.getServiceUrl().toString()).build();
    }
}
