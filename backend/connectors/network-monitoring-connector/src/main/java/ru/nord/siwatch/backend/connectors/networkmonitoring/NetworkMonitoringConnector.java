package ru.nord.siwatch.backend.connectors.networkmonitoring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.nord.siwatch.backend.connectors.networkmonitoring.models.Network;
import ru.nord.siwatch.backend.connectors.networkmonitoring.models.NetworkInfo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class NetworkMonitoringConnector {

    private final RestTemplate restTemplate;

    public NetworkMonitoringConnector(RestTemplateBuilder restTemplateBuilder, NetworkMonitoringConnectorSettings settings) {
        this.restTemplate = restTemplateBuilder.rootUri(settings.getServiceUrl().toString()).build();
    }

    public void save(NetworkInfo networkInfo) {
        restTemplate.postForObject("/network_mon/", networkInfo, String.class);
    }

    public List<Network> find(String deviceId, LocalDateTime fromTime, LocalDateTime toTime) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("deviceId", deviceId);
        if (fromTime != null) {
            params.add("fromTime", DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(fromTime));
        }
        if (toTime != null) {
            params.add("toTime", DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(toTime));
        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/network_mon/").queryParams(params);
        Network[] networks = restTemplate.getForObject(builder.build().toString(), Network[].class, params);
        return Arrays.asList(networks);
    }

}
