package ru.nord.siwatch.backend.connectors.batterymonitoring;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.nord.siwatch.backend.connectors.batterymonitoring.models.BatteryLevelInfo;

import java.net.URI;

@Component
public class BatteryMonitoringConnector
{
    private final RestTemplate restTemplate;

    public BatteryMonitoringConnector(RestTemplateBuilder restTemplateBuilder, BatteryMonitoringConnectorSettings settings)
    {
        this.restTemplate = restTemplateBuilder.rootUri(settings.getServiceUrl().toString()).build();
    }

    public void save(BatteryLevelInfo info) {
        restTemplate.put("/", info);
    }
}
