package com.watchwise.trakt;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TraktApiClient {

    private final RestTemplate restTemplate;
    private final TraktApiProperties properties;

    @Autowired
    public TraktApiClient(RestTemplateBuilder builder, TraktApiProperties properties) {
        this(builder.rootUri(properties.baseUrl()).build(), properties);
    }

    TraktApiClient(RestTemplate restTemplate, TraktApiProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    public String searchMovie(String query) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("trakt-api-key", properties.clientId());
        headers.set("trakt-api-version", "2");
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "/search/movie?query={query}", HttpMethod.GET, entity, String.class, query);
        return response.getBody();
    }
}
