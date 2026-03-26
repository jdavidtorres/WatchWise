package co.com.jdti.trakt;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

class TraktApiClientTest {

	private MockRestServiceServer server;
	private TraktApiClient client;

	@BeforeEach
	void setUp() {
		TraktApiProperties props = new TraktApiProperties("http://localhost", "test-key");
		RestTemplate restTemplate = new RestTemplateBuilder().rootUri(props.baseUrl()).build();
		this.server = MockRestServiceServer.createServer(restTemplate != null ? restTemplate : new RestTemplate());
		this.client = new TraktApiClient(restTemplate, props);
	}

	@Test
	void searchMovieSendsRequiredHeaders() {
		this.server.expect(MockRestRequestMatchers.requestTo("http://localhost/search/movie?query=inception"))
				.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
				.andExpect(MockRestRequestMatchers.header("trakt-api-key", "test-key"))
				.andExpect(MockRestRequestMatchers.header("trakt-api-version", "2"))
				.andRespond(MockRestResponseCreators.withSuccess("[]", MediaType.APPLICATION_JSON));

		String response = this.client.searchMovie("inception");
		Assertions.assertThat(response).isEqualTo("[]");
	}
}
