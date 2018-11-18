package ru.nord.siwatch.backend.connectors.locationmonitoring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.nord.backend.infrastructure.utils.MapBuilder;
import ru.nord.siwatch.backend.connectors.locationmonitoring.models.Location;
import ru.nord.siwatch.backend.connectors.locationmonitoring.models.LocationInfo;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class LocationMonitoringConnector
{
    private final RestTemplate restTemplate;

    public LocationMonitoringConnector(RestTemplateBuilder restTemplateBuilder, LocationMonitoringConnectorSettings settings)
    {
        this.restTemplate = restTemplateBuilder.rootUri(settings.getServiceUrl().toString()).build();
    }

    public void save(LocationInfo info) {
        restTemplate.put("/", info);
    }

    public List<Location> find(String deviceId, LocalDateTime fromTime, LocalDateTime toTime) {
        List<Location> location;
        try {
            location =
                restTemplate.getForObject(
                    "/",  List.class,
                    MapBuilder.put("deviceId", (Object) deviceId).put("fromTime", fromTime).put("toTime", toTime).build());
        }
        catch (RestClientException ex) {
            log.error("", ex);
            throw ex;
        }
        return location;
    }
}
