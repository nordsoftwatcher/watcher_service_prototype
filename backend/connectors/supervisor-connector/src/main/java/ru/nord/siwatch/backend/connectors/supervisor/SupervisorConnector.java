package ru.nord.siwatch.backend.connectors.supervisor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.nord.siwatch.backend.connectors.supervisor.model.Supervisor;

@Component
@Slf4j
public class SupervisorConnector {

    private final RestTemplate restTemplate;

    public SupervisorConnector(RestTemplateBuilder restTemplateBuilder, SupervisorConnectorSettings settings) {
        this.restTemplate = restTemplateBuilder.rootUri(settings.getServiceUrl().toString()).build();
    }

    public Supervisor getSupervisorById(Long supervisorId) {
        return restTemplate.getForObject("/supervisor/" + supervisorId, Supervisor.class);
    }

    public Supervisor getSupervisorByDeviceId(String deviceId) {
        return restTemplate.getForObject("/supervisor/by_device_id/" + deviceId, Supervisor.class);
    }

}
