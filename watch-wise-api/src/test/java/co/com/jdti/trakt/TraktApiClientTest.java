package co.com.jdti.trakt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

class TraktApiClientTest {

    private MockRestServiceServer server;
    private TraktApiClient client;

    @BeforeEach
    void setUp() {
        TraktApiProperties props = new TraktApiProperties("http://localhost", "test-key");
        RestTemplate restTemplate = new RestTemplateBuilder().rootUri(props.baseUrl()).build();
        server = MockRestServiceServer.createServer(restTemplate);
        client = new TraktApiClient(restTemplate, props);
    }

    @Test
    void searchMovieSendsRequiredHeaders() {
        server.expect(requestTo("http://localhost/search/movie?query=inception"))
            .andExpect(method(HttpMethod.GET))
            .andExpect(header("trakt-api-key", "test-key"))
            .andExpect(header("trakt-api-version", "2"))
            .andRespond(withSuccess("[]", MediaType.APPLICATION_JSON));

        String response = client.searchMovie("inception");
        assertThat(response).isEqualTo("[]");
    }
}
