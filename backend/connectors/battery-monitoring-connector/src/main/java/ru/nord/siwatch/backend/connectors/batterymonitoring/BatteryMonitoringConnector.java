package ru.nord.siwatch.backend.connectors.batterymonitoring;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.nord.siwatch.backend.connectors.batterymonitoring.models.BatteryLevel;
import ru.nord.siwatch.backend.connectors.batterymonitoring.models.BatteryLevelInfo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class BatteryMonitoringConnector
{
    private final RestTemplate restTemplate;

    public BatteryMonitoringConnector(RestTemplateBuilder restTemplateBuilder, BatteryMonitoringConnectorSettings settings)
    {
        this.restTemplate = restTemplateBuilder.rootUri(settings.getServiceUrl().toString()).build();
    }

    public void save(BatteryLevelInfo info) {
        restTemplate.postForObject("/battmon/", info, String.class);
    }

    public List<BatteryLevel> find(String deviceId, LocalDateTime fromTime, LocalDateTime toTime) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("deviceId", deviceId);
        params.add("fromTime", DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(fromTime));
        params.add("toTime", DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(toTime));

        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/battmon/").queryParams(params);
        return restTemplate.getForObject(builder.build().toString(), List.class, params);
    }
}
