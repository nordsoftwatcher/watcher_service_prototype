package ru.nord.siwatch.backend.connectors.event;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.nord.siwatch.backend.connectors.event.models.Event;
import ru.nord.siwatch.backend.connectors.event.models.EventInfo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Component
public class EventConnector {

    private final RestTemplate restTemplate;

    public EventConnector(RestTemplateBuilder restTemplateBuilder, EventConnectorSettings settings) {
        this.restTemplate = restTemplateBuilder.rootUri(settings.getServiceUrl().toString()).build();
    }

    public Event save(EventInfo eventInfo) {
        return restTemplate.postForObject("/event/", eventInfo , Event.class);
    }

    public Event getLastEventByTypeAndSupervisorId(String eventType, Long supervisorId) {
        return restTemplate.getForObject("/event/" + eventType + "/" + supervisorId, Event.class);
    }

    public List<Event> find(Long supervisorId, LocalDateTime fromTime, LocalDateTime toTime) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        if (supervisorId != null) {
            params.add("supervisorId", supervisorId.toString());
        }
        if (fromTime != null) {
            params.add("fromTime", DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(fromTime));
        }
        if (toTime != null) {
            params.add("toTime", DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(toTime));
        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/event/search").queryParams(params);
        Event[] events = restTemplate.getForObject(builder.build().toString(), Event[].class, params);
        return Arrays.asList(events);
    }

}
