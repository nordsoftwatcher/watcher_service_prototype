package ru.nord.siwatch.backend.connectors.event;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class EventConnector {

    private final RestTemplate restTemplate;

    public EventConnector(RestTemplateBuilder restTemplateBuilder, EventConnectorSettings settings) {
        this.restTemplate = restTemplateBuilder.rootUri(settings.getServiceUrl().toString()).build();
    }

}
