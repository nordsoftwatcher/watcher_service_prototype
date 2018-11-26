package ru.nord.siwatch.backend.connectors.event;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.nord.siwatch.backend.connectors.event.models.Event;
import ru.nord.siwatch.backend.connectors.event.models.EventInfo;

@Component
public class EventConnector {

    private final RestTemplate restTemplate;

    public EventConnector(RestTemplateBuilder restTemplateBuilder, EventConnectorSettings settings) {
        this.restTemplate = restTemplateBuilder.rootUri(settings.getServiceUrl().toString()).build();
    }

    public Event save(EventInfo eventInfo) {
        return restTemplate.postForObject("/event/", eventInfo , Event.class);
    }

}
