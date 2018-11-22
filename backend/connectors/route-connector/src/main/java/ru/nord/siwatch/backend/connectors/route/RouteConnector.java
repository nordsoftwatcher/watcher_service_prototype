package ru.nord.siwatch.backend.connectors.route;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.nord.siwatch.backend.connectors.route.models.Route;
import ru.nord.siwatch.backend.connectors.route.models.RouteInfo;

@Component
@Slf4j
public class RouteConnector {

    private final RestTemplate restTemplate;

    public RouteConnector(RestTemplateBuilder restTemplateBuilder, RouteConnectorSettings settings) {
        this.restTemplate = restTemplateBuilder.rootUri(settings.getServiceUrl().toString()).build();
    }

    public Route save(RouteInfo routeInfo) {
        return restTemplate.postForObject("/routes/", routeInfo , Route.class);
    }

    public Route getRoute(Long routeId) {
        return restTemplate.getForObject("/routes/" + routeId, Route.class);
    }

}
