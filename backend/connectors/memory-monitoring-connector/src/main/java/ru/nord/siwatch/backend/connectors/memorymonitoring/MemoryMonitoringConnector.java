package ru.nord.siwatch.backend.connectors.memorymonitoring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.nord.siwatch.backend.connectors.memorymonitoring.models.Memory;
import ru.nord.siwatch.backend.connectors.memorymonitoring.models.MemoryInfo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@Slf4j
public class MemoryMonitoringConnector {

    private final RestTemplate restTemplate;

    public MemoryMonitoringConnector(RestTemplateBuilder restTemplateBuilder, MemoryMonitoringConnectorSettings settings) {
        this.restTemplate = restTemplateBuilder.rootUri(settings.getServiceUrl().toString()).build();
    }

    public void save(MemoryInfo memoryInfo) {
        restTemplate.postForObject("/memory_mon/", memoryInfo, String.class);
    }

    public List<Memory> find(String deviceId, LocalDateTime fromTime, LocalDateTime toTime) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("deviceId", deviceId);
        if (fromTime != null) {
            params.add("fromTime", DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(fromTime));
        }
        if (toTime != null) {
            params.add("toTime", DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(toTime));
        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/memory_mon/").queryParams(params);
        return restTemplate.getForObject(builder.build().toString(), List.class, params);
    }

}
