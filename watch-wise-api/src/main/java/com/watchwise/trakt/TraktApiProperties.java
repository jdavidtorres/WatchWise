package com.watchwise.trakt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "trakt.api")
public record TraktApiProperties(String baseUrl, String clientId) {
}
