package ru.nord.siwatch.backend.connectors.locationmonitoring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.nord.siwatch.backend.connectors.locationmonitoring.models.Location;
import ru.nord.siwatch.backend.connectors.locationmonitoring.models.LocationInfo;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@Slf4j
public class LocationMonitoringConnector {
    private final RestTemplate restTemplate;

    public LocationMonitoringConnector(RestTemplateBuilder restTemplateBuilder, LocationMonitoringConnectorSettings settings) {
        this.restTemplate = restTemplateBuilder.rootUri(settings.getServiceUrl().toString()).build();
    }

    public void save(LocationInfo info) {
        restTemplate.postForObject("/locmon/", info, String.class);
    }

    public List<Location> find(String deviceId, LocalDateTime fromTime, LocalDateTime toTime) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("deviceId", deviceId);
        params.add("fromTime", DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(fromTime));
        params.add("toTime", DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(toTime));

        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/locmon/").queryParams(params);
        return restTemplate.getForObject(builder.build().toString(), List.class, params);
    }
}
