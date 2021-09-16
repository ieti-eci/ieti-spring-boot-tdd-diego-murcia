package org.adaschool.tdd;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.Date;
import java.util.Optional;

import org.adaschool.tdd.repository.WeatherReportRepository;
import org.adaschool.tdd.repository.document.GeoLocation;
import org.adaschool.tdd.repository.document.WeatherReport;
import org.adaschool.tdd.service.MongoWeatherService;
import org.adaschool.tdd.service.WeatherService;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WeatherReportControllerTest {
    private WeatherService weatherService;

	@MockBean
	private WeatherReportRepository repository;

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@BeforeEach()
	void setup() {
		weatherService = new MongoWeatherService(repository);
	}

	@Test
	void whenFindByIdEndpointIsReached_shouldReturnValidWeatherReport() throws Exception {
        WeatherReport weatherReport = new WeatherReport(new GeoLocation(4.7710, 74.0721), 35f, 22f, "tester", new Date());

		when(repository.findById("123456789")).thenReturn(Optional.of(weatherReport));

		final String baseUrl = "http://localhost:"+port+"/v1/weather" + "/123456789";
		URI uri = new URI(baseUrl);

		ResponseEntity<String> result = this.restTemplate.getForEntity(uri, String.class);

		assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());
	}
}
