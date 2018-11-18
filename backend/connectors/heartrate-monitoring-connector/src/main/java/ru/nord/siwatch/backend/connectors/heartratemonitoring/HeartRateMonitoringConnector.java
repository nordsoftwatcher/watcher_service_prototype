package ru.nord.siwatch.backend.connectors.heartratemonitoring;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.nord.siwatch.backend.connectors.heartratemonitoring.models.HeartRateInfo;

import java.net.URI;

@Component
public class HeartRateMonitoringConnector
{
    private final RestTemplate restTemplate;

    public HeartRateMonitoringConnector(RestTemplateBuilder restTemplateBuilder, HeartRateMonitoringConnectorSettings settings)
    {
        this.restTemplate = restTemplateBuilder.rootUri(settings.getServiceUrl().toString()).build();
    }

    public void save(HeartRateInfo info) {
        restTemplate.put("/", info);
    }
}
