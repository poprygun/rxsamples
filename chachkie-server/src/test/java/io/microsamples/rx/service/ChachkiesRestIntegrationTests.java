package io.microsamples.rx.service;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.function.context.test.FunctionalSpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

@FunctionalSpringBootTest
@AutoConfigureWebTestClient
class ChachkiesRestIntegrationTests {

	@Autowired
	private WebTestClient client;

	@MockBean
	private ChachkieRepository chachkieRepository;

	private EasyRandom randomGenerator = new EasyRandom();

	@BeforeEach
	void setUp(){
		BDDMockito.given(chachkieRepository.findAll())
				.willReturn(Flux.fromStream(randomGenerator.objects(Chachkie.class, 9)));
	}

	@Test
	void shouldGetChachkies() {
		client.get().uri("/chachkies")
				.accept(MediaType.APPLICATION_JSON)
				.exchange().expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.length()").isEqualTo(9)
				.jsonPath("$.[0].id").isNotEmpty()
				.jsonPath("$.[0].latitude").isNumber()
				.jsonPath("$.[0].longitude").isNumber();
	}
}
