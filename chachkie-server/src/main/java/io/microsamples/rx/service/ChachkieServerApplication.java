package io.microsamples.rx.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jeasy.random.EasyRandom;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.UUID;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@SpringBootApplication
public class ChachkieServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChachkieServerApplication.class, args);
	}

	@Bean
	RouterFunction<ServerResponse> routes(ChachkieRepository chachkieRepository) {
		return route()
				.GET("/chachkies", request -> ok().body(chachkieRepository.findAll(), Chachkie.class))
				.build();
	}
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Chachkie {
	private UUID id;
	private double latitude;
	private double longitude;
	private Instant when;
}

@Component
class ChachkieRepository{
	private EasyRandom randomGenerator = new EasyRandom();

	public Flux<Chachkie> findAll() {
		return Flux.fromStream(randomGenerator.objects(Chachkie.class, 13));
	}
}
