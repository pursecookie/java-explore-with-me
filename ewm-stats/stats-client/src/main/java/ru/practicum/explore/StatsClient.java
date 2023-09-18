package ru.practicum.explore;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.explore.models.EndpointHit;
import ru.practicum.explore.models.ViewStats;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class StatsClient {
    private final RestTemplate rest;
    private final String serverUrl;

    public StatsClient(@Value("${stats-server.url}") String serverUrl) {
        this.rest = new RestTemplate();
        this.serverUrl = serverUrl;
    }

    public void create(EndpointHit endpointHit) {
        rest.exchange(serverUrl + "/hit",
                HttpMethod.POST,
                new HttpEntity<>(endpointHit),
                Object.class);
    }

    public List<ViewStats> readAll(String start, String end, List<String> uris, Boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", encodeValue(start),
                "end", encodeValue(end),
                "uris", uris.toArray(),
                "unique", unique
        );

        ResponseEntity<List<ViewStats>> response = rest
                .exchange(serverUrl + "/stats?start={start}&end={end}&uris={uris}&unique={unique}",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        },
                        parameters);

        return response.getBody();
    }

    private String encodeValue(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}